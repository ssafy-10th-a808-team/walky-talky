package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.Chat;
import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import com.ssafy.backend.chat.domain.dto.MessageType;
import com.ssafy.backend.chat.repository.ChatMessageRepository;
import com.ssafy.backend.chat.repository.ChatRepository;
import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    //    private final RedisTemplate<String, ChatMessageDto> redisTemplateMessage;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChannelTopic topic;
    private final RedisPublisher redisPublisher;
//    private static final String CHAT_ROOM = "chat";

    @Override
    public void saveMessage(ChatMessageDto chatMessageDto) {
//        캐싱 적용
//        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
//        redisTemplateMessage.opsForList().rightPush(CHAT_ROOM + "-" + chatMessage.getChatSeq() + ":", chatMessage);

        if (MessageType.JOIN.equals(chatMessageDto.getType())) {
            chatMessageDto.setContent(chatMessageDto.getSender() + "님이 입장하셨습니다.");
        } else if (MessageType.LEAVE.equals(chatMessageDto.getType())) {
            chatMessageDto.setContent(chatMessageDto.getSender() + "님이 나가셨습니다.");
        }
        redisPublisher.publish(topic, chatMessageDto);
        Chat findChat = chatRepository.findByClubSeq(chatMessageDto.getClubSeq());
        if (findChat == null) {
            throw new WTException("존재하지 않는 채팅방입니다.");
        }
        ChatMessage chatMessage = ChatMessage.builder()
                .chat(findChat)
                .sender(chatMessageDto.getSender())
                .content(chatMessageDto.getContent())
                .createdAt(chatMessageDto.getCreatedAt())
                .clubSeq(chatMessageDto.getClubSeq())
                .type(chatMessageDto.getType())
                .build();

        chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessageDto> loadMessage(Long memberSeq, Long clubSeq, int offset) {
        List<ChatMessageDto> messageList = new ArrayList<>();
//        캐싱 적용
//        List<ChatMessageDto> redisMessageList = redisTemplateMessage.opsForList().range(CHAT_ROOM + "-" + chatSeq + ":", offset * 10, (offset * 10) + 9);
//        if (redisMessageList == null || redisMessageList.isEmpty()) {
//            List<ChatMessage> dbMessageList = chatMessageRepository.findByChatSeqOrderByCreatedAt(chatSeq);
//            int cnt = 0;
//            for (ChatMessage chatMessage : dbMessageList) {
//                if (cnt == 10) break;
//                redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
//                ChatMessageDto requestChatMessageDto = new ChatMessageDto(chatSeq, chatMessage.getSender(), chatMessage.getContent(), chatMessage.getCreatedAt(), chatMessage.getType());
//                redisTemplateMessage.opsForList().rightPush(CHAT_ROOM + "-" + chatSeq + ":", requestChatMessageDto);
//                messageList.add(requestChatMessageDto);
//                cnt++;
//            }
//        } else {
//            messageList.addAll(redisMessageList);
//        }
        Pageable pageable = PageRequest.of(offset, 10);
        List<ChatMessage> dbMessageList = chatMessageRepository.findByClubSeqOrderByCreatedAtDesc(clubSeq, pageable);
        Member findMember = memberRepository.findById(memberSeq).orElse(null);
        ChatMessage findJoinMessage;
        if (findMember == null) {
            return null;
        }
        findJoinMessage = chatMessageRepository.findFirstChatMessageByClubSeqAndSenderAndTypeOrderByCreatedAtDesc(clubSeq, findMember.getNickname(), MessageType.JOIN);

        LocalDateTime findJoinMessageCreatedAt = null;

        if (findJoinMessage != null) {
            findJoinMessageCreatedAt = LocalDateTime.parse(findJoinMessage.getCreatedAt());
        }

        for (ChatMessage chatMessage : dbMessageList) {
            LocalDateTime chatMessageCreatedAt = LocalDateTime.parse(chatMessage.getCreatedAt());
            if (findJoinMessageCreatedAt != null
                    && (chatMessageCreatedAt.isEqual(findJoinMessageCreatedAt)
                    || chatMessageCreatedAt.isBefore(findJoinMessageCreatedAt))) {
                break;
            }
            ChatMessageDto requestChatMessageDto = new ChatMessageDto(clubSeq, chatMessage.getSender(), chatMessage.getContent(), chatMessage.getCreatedAt(), chatMessage.getType());
            messageList.add(requestChatMessageDto);
        }
        return messageList;
    }
}
