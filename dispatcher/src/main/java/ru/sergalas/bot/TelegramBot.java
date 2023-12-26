package ru.sergalas.bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.sergalas.controller.UpdateController;

@Log4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String name;
    private UpdateController controller;

    public TelegramBot(@Value("${bot.token}") String botToken, UpdateController controller) throws TelegramApiException {
        super(botToken);
        this.controller = controller;
    }

    @PostConstruct
    public void init() {
        this.controller.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        controller.processUpdate(update);
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
}
