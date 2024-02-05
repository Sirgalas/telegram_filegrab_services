package ru.sergalas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sergalas.entity.AppUser;
import ru.sergalas.repository.AppUserRepository;
import ru.sergalas.service.interfaces.UserActivationService;
import ru.sergalas.utils.CryptoTool;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {

    private final AppUserRepository appUserRepository;
    private final CryptoTool cryptoTool;

    @Override
    public boolean activation(String cryptoUserId) {
        Long userId = cryptoTool.idOf(cryptoUserId);
        Optional<AppUser> optional = appUserRepository.findById(userId);
        if(optional.isPresent()) {
            AppUser user = optional.get();
            user.setIsActive(true);
            appUserRepository.save(user);
            return true;
        }
        return false;
    }
}
