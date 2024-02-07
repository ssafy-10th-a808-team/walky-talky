package com.ssafy.backend.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, ChatMessageDto> redisTemplateMessage;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplateMessage.getStringSerializer().deserialize(message.getBody());

            ChatMessageDto chatMessage = objectMapper.readValue(publishMessage, ChatMessageDto.class);
            messagingTemplate.convertAndSend("/sub/chat/" + chatMessage.getClubSeq(), chatMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
