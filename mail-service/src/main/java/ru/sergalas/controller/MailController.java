package ru.sergalas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sergalas.date.MailParamsData;
import ru.sergalas.service.interfaces.MailSenderService;

@RequestMapping("/mail")
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailSenderService mailSenderService;

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParamsData mailParamsData) {
        mailSenderService.send(mailParamsData);
        return ResponseEntity.ok().build();
    }
}
