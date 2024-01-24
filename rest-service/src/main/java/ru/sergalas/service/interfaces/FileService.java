package ru.sergalas.service.interfaces;

import org.springframework.core.io.FileSystemResource;
import ru.sergalas.entity.AppDocument;
import ru.sergalas.entity.AppPhoto;
import ru.sergalas.entity.BinaryContent;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
