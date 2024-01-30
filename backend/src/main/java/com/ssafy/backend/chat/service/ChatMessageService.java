package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.ChatMessage;
import com.ssafy.backend.clubMember.domain.ClubMember;

import java.util.List;

public interface ChatMessageService {
    void saveMessage(ChatMessage chatMessage);
    List<ChatMessage> loadMessage(Long chatSeq);
}
