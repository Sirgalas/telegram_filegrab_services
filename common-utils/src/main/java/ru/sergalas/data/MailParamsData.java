package ru.sergalas.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailParamsData {
    private String id;
    private String emailTo;
}
