package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.chat.repository.ChatMessageRepository;
import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.clubMember.domain.ClubMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final RedisMessageListenerContainer redisContainer;

    @Override
    public void sendMessage(ChatMessage chatMessage, ClubMember clubMember) {
        chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> getChatMessages(Long chatSeq, Pageable pageable) {
        return chatMessageRepository.findByChatSeqOrderByCreatedAt(chatSeq);
    }

    @Override
    public void createClubChat(Club club) {
        ChannelTopic channelTopic = new ChannelTopic(club.getSeq().toString());
//        redisContainer.addMessageListener(new RedisSubscriber(), channelTopic);
    }
}
