package com.ssafy.backend.chat.controller;

import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import com.ssafy.backend.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/message")
    public void message(ChatMessageDto chatMessage) {
        chatMessage.setCreatedAt(LocalDateTime.now().toString());
        chatMessageService.saveMessage(chatMessage);
    }
}
