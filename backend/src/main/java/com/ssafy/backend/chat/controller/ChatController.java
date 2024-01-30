package com.ssafy.backend.chat.controller;

import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.chat.service.ChatMessageService;
import com.ssafy.backend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    @PostMapping("/chat/{clubSeq}")
    public void createChatRoom(@PathVariable Long clubSeq) {
        chatService.createChatRoom(clubSeq);
    }

//    @GetMapping("/chat")
//    public List<Chat> findAllChatRoomByUser(Member member) {
//        return chatService.findAllChatRoomByUser(member);
//    }

    @GetMapping("/chat/{chatSeq}/message")
    public List<ChatMessage> loadMessage(@PathVariable Long chatSeq) {
        return chatMessageService.loadMessage(chatSeq);
    }
}
