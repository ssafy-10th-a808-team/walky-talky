package com.ssafy.backend.chat.repository;

import com.ssafy.backend.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findOneByClubSeq(Long clubSeq);
}
