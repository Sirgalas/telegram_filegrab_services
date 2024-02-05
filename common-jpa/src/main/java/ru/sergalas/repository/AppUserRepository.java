package ru.sergalas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sergalas.entity.AppUser;
import ru.sergalas.enums.UserState;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findByTelegramUserId(Long id);
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByEmail(String email);
}
