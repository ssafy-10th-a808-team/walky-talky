package com.ssafy.backend.chat.controller;

import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.chat.repository.ChatRedisRepository;
import com.ssafy.backend.chat.service.ChatService;
import com.ssafy.backend.chat.service.RedisPublisher;
import com.ssafy.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final ChatRedisRepository chatRedisRepository;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessage chatMessage, Member member, Long seq) {
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())){
            chatMessage.setContent(member.getNickname() + "님이 입장하셨습니다.");
        } else if (ChatMessage.MessageType.EXIT.equals(chatMessage.getType())) {
            chatMessage.setContent(member.getNickname() + "님이 퇴장하셨습니다.");
        }
        redisPublisher.publish(chatRedisRepository.getTopic(seq), chatMessage);
    }

//    @GetMapping("/api/chat/{chatSeq}/messages")
//    public List<ChatMessage> getChatMessages(
//            @PathVariable Long chatSeq,
//            @PageableDefault(sort = "createdAt")Pageable pageable) {
//        return chatService.getChatMessages(chatSeq, pageable);
//    }
}
