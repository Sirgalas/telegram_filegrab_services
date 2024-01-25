package ru.sergalas.service.interfaces;

import ru.sergalas.date.MailParamsData;

public interface MailSenderService {
    void send(MailParamsData mailParamsData);
}
