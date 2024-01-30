package com.ssafy.backend.chat.controller;

import com.ssafy.backend.chat.domain.Chat;
import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.chat.service.ChatMessageService;
import com.ssafy.backend.chat.service.ChatService;
import com.ssafy.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    //    @MessageMapping("/chat/message")
//    public void sendMessage(ChatMessage chatMessage, Member member, Long seq) {
//        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())){
//            chatMessage.setContent(member.getNickname() + "님이 입장하셨습니다.");
//        } else if (ChatMessage.MessageType.EXIT.equals(chatMessage.getType())) {
//            chatMessage.setContent(member.getNickname() + "님이 퇴장하셨습니다.");
//        }
//        redisPublisher.publish(chatService.getTopic(seq), chatMessage);
//    }
    @PostMapping("/chat/{clubSeq}")
    public void createChatRoom(@PathVariable Long clubSeq) {
        chatService.createChatRoom(clubSeq);
    }

//    @GetMapping("/chat")
//    public List<Chat> findAllChatRoomByUser(Member member) {
//        return chatService.findAllChatRoomByUser(member);
//    }


    @GetMapping("/api/chat/{chatSeq}/message")
    public List<ChatMessage> loadMessage(@PathVariable Long chatSeq) {
        return chatMessageService.loadMessage(chatSeq);
    }
}
