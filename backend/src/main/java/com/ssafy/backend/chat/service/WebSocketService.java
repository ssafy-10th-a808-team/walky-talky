package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getChatSeq(), chatMessage);
    }

}
