package ru.sergalas.bot;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String name;

    @Override
    public void onUpdateReceived(Update update) {
        var originalMessage = update.getMessage();
        log.debug(originalMessage.getText());
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    public TelegramBot(@Value("${bot.token}") String botToken) throws TelegramApiException {
        super(botToken);
    }
}
