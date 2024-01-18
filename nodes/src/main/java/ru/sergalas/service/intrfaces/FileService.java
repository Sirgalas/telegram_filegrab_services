package ru.sergalas.service.intrfaces;

import org.apache.logging.log4j.message.Message;
import ru.sergalas.entity.AppDocument;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}
