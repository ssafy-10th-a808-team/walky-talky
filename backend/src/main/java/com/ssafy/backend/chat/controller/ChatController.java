package com.ssafy.backend.chat.controller;

import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import com.ssafy.backend.chat.service.ChatMessageService;
import com.ssafy.backend.chat.service.ChatService;
import com.ssafy.backend.clubMember.repository.ClubMemberRepository;
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
    private final ClubMemberRepository clubMemberRepository;
    private static final List<String> ROLE = List.of("owner", "member");

    @PostMapping("/{clubSeq}")
    public void createChatRoom(@PathVariable Long clubSeq) {
        chatService.createChatRoom(clubSeq);
    }

    @GetMapping("/{clubSeq}/{offset}")
    public ResponseEntity<?> loadMessage(HttpServletRequest request, @PathVariable(name = "clubSeq") Long clubSeq, @PathVariable(name = "offset") int offset) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        Long memberSeq = (Long) request.getAttribute("seq");
        if (memberSeq == null) {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }

        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRoleIn(clubSeq, memberSeq, ROLE)) {
            resultMap.put("message", "소모임에 가입해주세요.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }

        Map<String, List<ChatMessageDto>> returnMap = new HashMap<>();
        returnMap.put("list", chatMessageService.loadMessage(memberSeq, clubSeq, offset));

        resultMap.put("message", "OK");
        resultMap.put("data", returnMap);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }
}
