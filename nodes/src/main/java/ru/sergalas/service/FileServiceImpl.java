package ru.sergalas.service;

import lombok.Value;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ru.sergalas.service.intrfaces.FileService;

@Log4j
@Service
public class FileServiceImpl implements FileService {
    @Value("${token}")
    private String token;

    @Value("${service.file.info.uri}")
    private String fileInfoUri;
}
