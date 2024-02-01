package com.ssafy.backend.chat.controller;

import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import com.ssafy.backend.chat.service.ChatMessageService;
import com.ssafy.backend.chat.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/{chatSeq}/{offset}")
    public ResponseEntity<?> loadMessage(HttpServletRequest request, @PathVariable Long chatSeq, @PathVariable int offset) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        Long memberSeq = (Long) request.getAttribute("seq");
        if (memberSeq == null) {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }

        Map<String, List<ChatMessageDto>> returnMap = new HashMap<>();
        returnMap.put("list", chatMessageService.loadMessage(memberSeq, chatSeq, offset));

        resultMap.put("message", "OK");
        resultMap.put("data", returnMap);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }
}
