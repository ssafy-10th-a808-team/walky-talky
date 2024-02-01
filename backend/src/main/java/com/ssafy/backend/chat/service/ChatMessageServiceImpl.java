package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.Chat;
import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import com.ssafy.backend.chat.repository.ChatMessageRepository;
import com.ssafy.backend.chat.repository.ChatRepository;
import com.ssafy.backend.global.error.WTException;
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
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private static final String CHAT_ROOM = "chat";

    @Override
    public void saveMessage(ChatMessageDto chatMessageDto) {
//        캐싱 적용
//        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
//        redisTemplateMessage.opsForList().rightPush(CHAT_ROOM + "-" + chatMessage.getChatSeq() + ":", chatMessage);
        Chat findChat = chatRepository.findById(chatMessageDto.getChatSeq()).orElse(null);
        if (findChat == null) {
            throw new WTException("존재하지 않는 채팅방입니다.");
        }
        ChatMessage chatMessage = ChatMessage.builder()
                .chat(findChat)
                .sender(chatMessageDto.getSender())
                .content(chatMessageDto.getContent())
                .createdAt(chatMessageDto.getCreatedAt())
                .type(chatMessageDto.getType())
                .build();

        chatMessageRepository.save(chatMessage);
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
