package ru.sergalas.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sergalas.service.UpdateProducer;

@Service
@Log4j
public class UpdateProducerImpl implements UpdateProducer {

    @Override
    public void producer(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());
    }
}
