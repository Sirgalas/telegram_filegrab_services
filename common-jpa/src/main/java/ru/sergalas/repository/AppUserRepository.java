package ru.sergalas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sergalas.entity.AppUser;
import ru.sergalas.enums.UserState;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findAppUsesByTelegramUserId(Long Id);
}
