package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import com.ssafy.backend.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final RedisTemplate<String, ChatMessageDto> redisTemplateMessage;
    private final ChatMessageRepository chatMessageRepository;
    private static final String CHAT_ROOM = "chat";

    @Override
    public void saveMessage(ChatMessageDto chatMessage) {
        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
        redisTemplateMessage.opsForList().rightPush(CHAT_ROOM + "-" + chatMessage.getChatSeq() + ":", chatMessage);
    }

    @Override
    public List<ChatMessageDto> loadMessage(Long chatSeq, Long offset) {
        List<ChatMessageDto> messageList = new ArrayList<>();
        List<ChatMessageDto> redisMessageList = redisTemplateMessage.opsForList().range(CHAT_ROOM + "-" + chatSeq + ":", offset * 10, (offset * 10) + 9);
        if (redisMessageList == null || redisMessageList.isEmpty()) {
            List<ChatMessage> dbMessageList = chatMessageRepository.findByChatSeqOrderByCreatedAt(chatSeq);
            int cnt = 0;
            for (ChatMessage chatMessage : dbMessageList) {
                if (cnt == 10) break;
                redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
                ChatMessageDto requestChatMessageDto = new ChatMessageDto(chatSeq, chatMessage.getSender(), chatMessage.getContent(), chatMessage.getCreatedAt(), chatMessage.getType());
                redisTemplateMessage.opsForList().rightPush(CHAT_ROOM + "-" + chatSeq + ":", requestChatMessageDto);
                messageList.add(requestChatMessageDto);
                cnt++;
            }
        } else {
            messageList.addAll(redisMessageList);
        }
        return messageList;
    }
}
