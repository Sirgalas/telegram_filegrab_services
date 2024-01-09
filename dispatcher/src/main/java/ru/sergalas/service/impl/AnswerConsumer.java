package ru.sergalas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.sergalas.controller.UpdateController;
import ru.sergalas.handler.MessageHandler;

import static ru.sergalas.model.RabbitQueue.ANSWER_MESSAGE;

@RequiredArgsConstructor
@Service
public class AnswerConsumer implements ru.sergalas.service.AnswerConsumer {
    private final MessageHandler messageHandler;

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        messageHandler.setView(sendMessage);
    }
}
