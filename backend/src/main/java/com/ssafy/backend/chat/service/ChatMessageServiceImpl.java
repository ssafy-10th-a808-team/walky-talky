package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.Chat;
import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.chat.repository.ChatMessageRepository;
import com.ssafy.backend.chat.repository.ChatRepository;
import com.ssafy.backend.clubMember.domain.ClubMember;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final RedisTemplate<String, ChatMessage> redisTemplateMessage;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void saveMessage(ChatMessage chatMessage) {
        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        redisTemplateMessage.opsForList().rightPush(chatMessage.getChatSeq().toString(), chatMessage);
    }

    @Override
    public List<ChatMessage> loadMessage(Long chatSeq) {
        List<ChatMessage> messageList = new ArrayList<>();
        List<ChatMessage> redisMessageList = redisTemplateMessage.opsForList().range(chatSeq.toString(), 0, 99);
        if (redisMessageList == null || redisMessageList.isEmpty()) {
            List<ChatMessage> dbMessageList = chatMessageRepository.findByChatSeqOrderByCreatedAt(chatSeq);
            for (ChatMessage chatMessage : dbMessageList) {
                redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
                redisTemplateMessage.opsForList().rightPush(chatMessage.getChatSeq().toString(), chatMessage);
            }
        } else {
            messageList.addAll(redisMessageList);
        }
        return messageList;
    }
}
