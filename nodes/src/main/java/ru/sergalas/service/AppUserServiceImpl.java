package ru.sergalas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sergalas.data.MailParamsData;
import ru.sergalas.entity.AppUser;
import ru.sergalas.enums.UserState;
import ru.sergalas.repository.AppUserRepository;
import ru.sergalas.service.intrfaces.AppUserService;
import ru.sergalas.utils.CryptoTool;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Optional;

import static ru.sergalas.enums.UserState.BASIC_STATE;

@Log4j
@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final CryptoTool cryptoTool;
    @Value("$(service.mail.uri}")
    private String mailServiceUri;


    @Override
    public String registerUser(AppUser appUser) {
        if(appUser.getIsActive()) {
            return "Вы уже зарегистрированы!";
        }
        if(appUser.getEmail() != null) {
            return "Вам на почту уже было отправлено письмо. "
                    + "Перейдите по ссылке в письме для подтверждения регистрации.";
        }
        appUser.setState(UserState.WAIT_FOR_EMAIL_STATE);
        appUserRepository.save(appUser);
        return "Введите, пожалуйста, ваш email:";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try{
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
        Optional<AppUser> optional = appUserRepository.findByEmail(email);
        if(optional.isEmpty()) {
            appUser.setEmail(email);
            appUser.setState(BASIC_STATE);
            appUser = appUserRepository.save(appUser);

            String cryptoUserId = cryptoTool.hasOf(appUser.getId());
            ResponseEntity<String> response = sendRequestToMailService(cryptoUserId,email);
            if(response.getStatusCode() != HttpStatus.OK) {
                String msg = String.format("Отправка эл. письма на почту %s не удалась.", email);
                log.error(msg);
                appUser.setEmail(null);
                appUserRepository.save(appUser);
                return msg;
            }
            return "Вам на почту было отправлено письмо."
                    + "Перейдите по ссылке в письме для подтверждения регистрации.";
        } else {
            return "Этот email уже используется. Введите корректный email."
                    + " Для отмены команды введите /cancel";
        }
    }

    private ResponseEntity<String> sendRequestToMailService(String cryptoUserId, String email) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MailParamsData mailParams = MailParamsData.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();
        var request =  new HttpEntity<>(mailParams, headers);
        return restTemplate.exchange(
                mailServiceUri,
                HttpMethod.POST,
                request,
                String.class
            );
    }
}
