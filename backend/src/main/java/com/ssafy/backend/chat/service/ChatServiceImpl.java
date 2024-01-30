package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.Chat;
import com.ssafy.backend.chat.repository.ChatMessageRepository;
import com.ssafy.backend.chat.repository.ChatRepository;
import com.ssafy.backend.member.domain.Member;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;

    private static final String CHAT_ROOM = "CHAT";
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        topics = new HashMap<>();
    }
    public void createChatRoom(Long clubSeq) {
        // 생성된 채팅방인지 확인할 것
        Chat chatRoom = Chat.builder().clubSeq(clubSeq).build();
        // 채팅방 생성 chatRepository.save();
    }

    public void enterChatRoom(Long clubSeq) {
        ChannelTopic topic = topics.get(clubSeq.toString());
        if (topic == null) {
            topic = new ChannelTopic(clubSeq.toString());
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topics.put(clubSeq.toString(), topic);
        }
    }
}
