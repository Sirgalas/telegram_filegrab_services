package ru.sergalas.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sergalas.bot.TelegramBot;
import ru.sergalas.config.MessageUtils;
import ru.sergalas.handler.MessageHandler;
import ru.sergalas.service.UpdateProducer;

@Controller
@Log4j
@RequiredArgsConstructor
public class UpdateProcessor {

    private TelegramBot bot;

    private MessageHandler messageHandler;

    private final MessageUtils messageUtils;

    private final UpdateProducer updateProducer;


    public void  registerBot(TelegramBot bot) {
        this.bot = bot;
        this.messageHandler = new MessageHandler(bot, messageUtils, updateProducer);
    }

    public void processUpdate(Update update) {
        if(update == null) {
            log.error("Received update is null");
            return;
        }
        if(update.hasMessage()) {
            messageHandler.distributeMessage(update);
        } else {
            log.error("Received unsupported message type " + update);
        }
    }

}
