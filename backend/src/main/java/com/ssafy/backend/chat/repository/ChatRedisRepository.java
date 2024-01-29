package com.ssafy.backend.chat.repository;

import com.ssafy.backend.chat.domain.Chat;
import com.ssafy.backend.chat.service.RedisSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ChatRedisRepository {

    private static final String CHAT_ROOM = "CHAT:";
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Chat> opsHashChatRoom;
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<Chat> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOM);
    }
    public Chat findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOM, id);
    }

    public Chat createChatRoom(Long clubSeq) {
        Chat chatRoom = Chat.builder().clubSeq(clubSeq).build();
        opsHashChatRoom.put(CHAT_ROOM, chatRoom.getClubSeq().toString(), chatRoom);
        return chatRoom;
    }

    public void enterChatRoom(Long clubSeq) {
        ChannelTopic topic = topics.get(clubSeq);
        if (topic == null) {
            topic = new ChannelTopic(clubSeq.toString());
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topics.put(clubSeq.toString(), topic);
        }
    }

    public ChannelTopic getTopic(Long clubSeq) {
        return topics.get(clubSeq.toString());
    }
}
