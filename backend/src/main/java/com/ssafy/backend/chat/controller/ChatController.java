package com.ssafy.backend.chat.controller;

import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import com.ssafy.backend.chat.service.ChatMessageService;
import com.ssafy.backend.chat.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    @PostMapping("/{clubSeq}")
    public void createChatRoom(HttpServletRequest request, @PathVariable Long clubSeq) {
        chatService.createChatRoom(clubSeq);
    }

    @GetMapping("/{chatSeq}")
    public List<ChatMessageDto> loadMessage(@PathVariable Long chatSeq) {
        return chatMessageService.loadMessage(chatSeq);
    }
}
