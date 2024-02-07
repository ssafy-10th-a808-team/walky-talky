package com.ssafy.backend.chat.repository;

import com.ssafy.backend.chat.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByClubSeqOrderByCreatedAt(Long clubSeq, Pageable pageable);

    ChatMessage findByChatSeq(Long chatSeq);

}
