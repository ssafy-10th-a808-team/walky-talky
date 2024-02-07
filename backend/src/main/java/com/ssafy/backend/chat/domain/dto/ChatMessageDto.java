package com.ssafy.backend.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long clubSeq;
    private String sender;
    private String content;
    private String createdAt;
    private MessageType type;
}
