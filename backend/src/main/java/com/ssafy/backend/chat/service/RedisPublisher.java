package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, ChatMessageDto> redisTemplateMessage;

    public void publish(ChannelTopic topic, ChatMessageDto message) {
        redisTemplateMessage.convertAndSend(topic.getTopic(), message);
    }
}
