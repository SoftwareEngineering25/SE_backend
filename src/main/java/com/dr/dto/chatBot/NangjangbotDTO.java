package com.dr.dto.chatBot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NangjangbotDTO {
    private Long userNumber;
    private Long sessionNumber;
    private Long chatNumber;
    private String sessionTitle;
    private String userMsg;
    private String botReply;
    private LocalDateTime createDate;
}
