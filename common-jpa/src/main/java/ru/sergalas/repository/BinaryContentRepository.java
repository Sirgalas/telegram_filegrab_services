package ru.sergalas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sergalas.entity.BinaryContent;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
}
