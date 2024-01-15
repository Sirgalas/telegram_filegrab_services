package ru.sergalas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sergalas.entity.RawData;
import ru.sergalas.repository.RawDataRepository;
import ru.sergalas.service.intrfaces.MainService;
import ru.sergalas.service.intrfaces.ProducerService;

@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {

    private final RawDataRepository repository;
    private final ProducerService producerService;

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello from NODE");
        producerService.producerAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                            .update(update)
                            .build();
        repository.save(rawData);
    }
}
