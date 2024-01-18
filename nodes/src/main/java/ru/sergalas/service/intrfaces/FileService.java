package ru.sergalas.service.intrfaces;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sergalas.entity.AppDocument;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}
