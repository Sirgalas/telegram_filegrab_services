package ru.sergalas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import ru.sergalas.entity.AppDocument;
import ru.sergalas.entity.AppPhoto;
import ru.sergalas.entity.BinaryContent;
import ru.sergalas.repository.AppDocumentRepository;
import ru.sergalas.repository.AppPhotoRepository;
import ru.sergalas.service.interfaces.FileService;
import ru.sergalas.utils.CryptoTool;

import java.io.File;
import java.io.IOException;

@Log4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;
    private final CryptoTool cryptoTool;

    @Override
    public AppDocument getDocument(String hash) {
       var id = cryptoTool.idOf(hash);
       if(id == null) {
           return null;
       }
       return appDocumentRepository.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String hash) {
        Long id = cryptoTool.idOf(hash);
        if(id == null) {
            return null;
        }
        return appPhotoRepository.findById(id).orElse(null);
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try{
            File temp = File.createTempFile("tempFile",".bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}
