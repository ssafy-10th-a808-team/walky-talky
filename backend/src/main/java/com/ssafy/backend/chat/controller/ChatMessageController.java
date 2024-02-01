package com.ssafy.backend.chat.controller;

import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import com.ssafy.backend.chat.domain.dto.MessageType;
import com.ssafy.backend.chat.service.ChatMessageService;
import com.ssafy.backend.chat.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final RedisPublisher redisPublisher;
    private final ChatMessageService chatMessageService;
    private final ChannelTopic topic;

    @MessageMapping("/message")
    public void message(ChatMessageDto chatMessage) {
        if (MessageType.JOIN.equals(chatMessage.getType())) {
            chatMessage.setContent(chatMessage.getSender() + "님이 입장하셨습니다.");
        } else if (MessageType.LEAVE.equals(chatMessage.getType())) {
            chatMessage.setContent(chatMessage.getSender() + "님이 나가셨습니다.");
        }
        chatMessage.setCreatedAt(LocalDateTime.now().toString());
        redisPublisher.publish(topic, chatMessage);
        chatMessageService.saveMessage(chatMessage);
    }
}
