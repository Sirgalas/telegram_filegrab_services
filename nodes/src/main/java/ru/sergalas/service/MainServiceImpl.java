package ru.sergalas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.sergalas.entity.AppDocument;
import ru.sergalas.entity.AppPhoto;
import ru.sergalas.entity.AppUser;
import ru.sergalas.entity.RawData;
import ru.sergalas.enums.UserState;
import ru.sergalas.exceptions.UploadFileException;
import ru.sergalas.repository.AppUserRepository;
import ru.sergalas.repository.RawDataRepository;
import ru.sergalas.service.enums.LinkType;
import ru.sergalas.service.enums.ServiceCommand;
import ru.sergalas.service.intrfaces.AppUserService;
import ru.sergalas.service.intrfaces.FileService;
import ru.sergalas.service.intrfaces.MainService;
import ru.sergalas.service.intrfaces.ProducerService;

import java.util.Optional;

import static ru.sergalas.enums.UserState.BASIC_STATE;
import static ru.sergalas.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static ru.sergalas.service.enums.ServiceCommand.*;

@RequiredArgsConstructor
@Service
@Log4j
public class MainServiceImpl implements MainService {

    private final RawDataRepository repository;
    private final ProducerService producerService;
    private final AppUserRepository appUserRepository;
    private final FileService fileService;
    private final AppUserService appUserService;


    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        String output =  getOutput(update);
        Long chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveUser(update);
        Long chatId =  update.getMessage().getChatId();
        if(isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        try {
            AppDocument document = fileService.processDoc(update.getMessage());
            String link = fileService.generateLink(document.getId(), LinkType.GET_DOC);
            String answer = "Документ успешно загружен! "
                              + "Ссылка для скачивания: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException e) {
            log.error(e);
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }

    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveUser(update);
        Long chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        try{
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            String link = fileService.generateLink((photo.getId()), LinkType.GET_PHOTO);
            String answer = "Фото успено загружено! "
                    + "Ссылка для скачивания: " + link;
            sendAnswer(answer, chatId);
        }catch (UploadFileException e) {
            log.error(e);
            String error = "К сожалению, загрузка фото не удалась. Повторите попытку позже.";
            sendAnswer(error,chatId);
        }
    }

    private String getOutput(Update update) {

        AppUser appUser = findOrSaveUser(update);
        String text = update.getMessage().getText();
        UserState userState = appUser.getState();
        ServiceCommand serviceCommand = ServiceCommand.fromValue(text);
        if(CANCEL.equals(serviceCommand)) {
           return cancelProcess(appUser);
        }
        if(BASIC_STATE.equals(userState)) {
            return processServiceCommand(appUser, serviceCommand);
        }
        if(WAIT_FOR_EMAIL_STATE.equals(userState)) {
            return appUserService.setEmail(appUser,text);
        }
        log.error("Unknown user state: " + userState);
        return  "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        UserState userState = appUser.getState();
        if(!appUser.getIsActive()) {
            String error = "Зарегистрируйтесь или активируйте свою учетную запись для загрузки контента.";
            sendAnswer(error,chatId);
            return true;
        }
        if(!BASIC_STATE.equals(userState)) {
            String error = "Отмените текущую команду с помощью /cancel для отправки файлов.";
            sendAnswer(error,chatId);
            return true;
        }
        return false;
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, ServiceCommand command) {

        if (REGISTRATION.equals(command)) {
            //TODO добавить регистрацию
            return "Временно недоступно.";
        }

        if (HELP.equals(command)) {
            return help();
        }

        if (START.equals(command)) {
            return "Приветствую! Чтобы посмотреть список доступных команд введите /help";
        }

         return "Неизвестная команда! Чтобы посмотреть список доступных команд введите /help";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserRepository.save(appUser);
        return "Команда отменена";
    }

    private String help() {
        return "Список доступных команд:\n"
                + "/cancel - отмена выполнения текущей команды;\n"
                + "/registration - регистрация пользователя.";
    }

    private AppUser findOrSaveUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        Optional<AppUser> optional = appUserRepository.findByTelegramUserId(telegramUser.getId());
        if(optional.isEmpty()) {
            // сделать через dto и маперы
            AppUser transientUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .isActive(false)
                    .state(BASIC_STATE)
                    .build();
            return appUserRepository.save(transientUser);
        }
        return optional.get();
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                            .update(update)
                            .build();
        repository.save(rawData);
    }
}
