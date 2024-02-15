package com.ssafy.backend.chat.repository;

import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.chat.domain.dto.MessageType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByClubSeqOrderByCreatedAtDesc(Long clubSeq, Pageable pageable);

    ChatMessage findFirstChatMessageByClubSeqAndSenderAndTypeOrderByCreatedAtDesc(Long clubSeq, String Sender, MessageType type);
}
