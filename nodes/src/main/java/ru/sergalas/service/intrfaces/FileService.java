package ru.sergalas.service.intrfaces;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sergalas.entity.AppDocument;
import ru.sergalas.entity.AppPhoto;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
    AppPhoto processPhoto(Message telegramMessage);
}
