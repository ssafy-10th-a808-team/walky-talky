package com.ssafy.backend.chat.domain.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private Long clubSeq;
    private String sender;
    private String content;
    private String createdAt;
    private MessageType type;
}
