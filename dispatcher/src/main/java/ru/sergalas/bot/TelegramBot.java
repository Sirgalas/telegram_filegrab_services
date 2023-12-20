package ru.sergalas.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String name;

    @Override
    public void onUpdateReceived(Update update) {
        var originalMessage = update.getMessage();
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    public TelegramBot(@Value("${bot.token}") String botToken) throws TelegramApiException {
        super(botToken);
    }
}
