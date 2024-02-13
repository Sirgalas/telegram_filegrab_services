package ru.sergalas.service.interfaces;

import ru.sergalas.data.MailParamsData;

public interface MailSenderService {
    void send(MailParamsData mailParamsData);
}
