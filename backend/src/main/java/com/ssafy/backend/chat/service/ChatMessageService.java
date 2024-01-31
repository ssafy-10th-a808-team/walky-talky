package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.dto.ChatMessageDto;

import java.util.List;

public interface ChatMessageService {
    void saveMessage(ChatMessageDto chatMessage);
    List<ChatMessageDto> loadMessage(Long chatSeq);
}
