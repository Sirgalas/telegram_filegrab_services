package ru.sergalas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.sergalas.date.MailParamsData;
import ru.sergalas.service.interfaces.MailSenderService;

@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender javaMailSender;


    @Override
    public void send(MailParamsData mailParamsData) {

    }
}
