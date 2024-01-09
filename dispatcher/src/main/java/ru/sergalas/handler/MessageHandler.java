package ru.sergalas.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sergalas.bot.TelegramBot;
import ru.sergalas.config.MessageUtils;
import ru.sergalas.service.UpdateProducer;

import static ru.sergalas.model.RabbitQueue.*;

@Component
public class MessageHandler {


    private MessageUtils messageUtils;

    private TelegramBot telegramBot;

    private UpdateProducer updateProducer;

    public MessageHandler(
            TelegramBot telegramBot,
            MessageUtils messageUtils,
            UpdateProducer updateProducer) {
        this.telegramBot = telegramBot;
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void distributeMessage(Update update) {
        Message message = update.getMessage();
        if(message.getText() != null) {
            processTextMessage(update);
            return;
        }
        if(message.getDocument() != null) {
            processDocMessage(update);
            return;
        }
        if(message.getPhoto() != null) {
            processPhotoMessage(update);
            return;
        }
        setUnsupportedMessageTypeView(update);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(
                update,
                "Не поддерживаемый тип сообщения!");
        setView(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.producer(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processDocMessage(Update update) {
        updateProducer.producer(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.producer(TEXT_MESSAGE_UPDATE, update);
    }

    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(
                update,
                "Фаил получен! Дождитесь окончание обработки");
        setView(sendMessage);
    }
    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
}
