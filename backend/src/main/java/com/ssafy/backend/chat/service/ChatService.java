package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.clubMember.domain.ClubMember;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {
    void sendMessage(ChatMessage chatMessage, ClubMember clubMember);

    List<ChatMessage> getChatMessages(Long clubSeq, Pageable pageable);

    void createClubChat(Club club);
}
