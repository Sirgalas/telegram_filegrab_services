package ru.sergalas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ru.sergalas.entity.AppDocument;
import ru.sergalas.entity.AppPhoto;
import ru.sergalas.repository.AppDocumentRepository;
import ru.sergalas.repository.AppPhotoRepository;
import ru.sergalas.service.interfaces.FileService;
import ru.sergalas.utils.CryptoTool;

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

}
