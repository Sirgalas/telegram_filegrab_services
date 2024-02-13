package ru.sergalas.bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.sergalas.processors.UpdateProcessor;

@Log4j
@Component
public class TelegramBot extends TelegramWebhookBot {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.uri}")
    private String botUri;
    private UpdateProcessor controller;

    public TelegramBot(@Value("${bot.token}") String botToken, UpdateProcessor controller) throws TelegramApiException {
        super(botToken);
        this.controller = controller;
    }

    @PostConstruct
    public void init() {
        this.controller.registerBot(this);
        try {
            SetWebhook setWebhook = SetWebhook.builder()
                    .url(botUri)
                    .build();
            this.setWebhook(setWebhook);
        } catch (TelegramApiException e) {
            log.error(e);
        }
    }

    @Override
    public String getBotPath() {
        return "/update";
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    public void sendAnswerMessage(SendMessage message) {
        if(message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }


}
