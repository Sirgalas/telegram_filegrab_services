package ru.sergalas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sergalas.entity.AppPhoto;

public interface AppPhotoRepository extends JpaRepository<AppPhoto,Long> {
}
