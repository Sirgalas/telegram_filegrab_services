package ru.sergalas.service.intrfaces;

import ru.sergalas.entity.AppUser;

public interface AppUserService {
    String registerUser(AppUser appUser);
    String setEmail(AppUser appUser, String email);
}
