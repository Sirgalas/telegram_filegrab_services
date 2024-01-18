package ru.sergalas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sergalas.entity.AppDocument;

public interface AppDocumentRepository extends JpaRepository<AppDocument,Long> {
}
