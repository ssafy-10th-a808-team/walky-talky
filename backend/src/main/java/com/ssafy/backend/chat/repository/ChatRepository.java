package com.ssafy.backend.chat.repository;

import com.ssafy.backend.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Boolean existsChatByClubSeq(Long clubSeq);
}
