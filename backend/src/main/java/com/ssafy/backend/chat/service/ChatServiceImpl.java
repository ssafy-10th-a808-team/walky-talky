package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.Chat;
import com.ssafy.backend.chat.repository.ChatRepository;
import com.ssafy.backend.global.error.WTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    public void createChatRoom(Long clubSeq) throws WTException {
        if (chatRepository.existsChatByClubSeq(clubSeq)) {
            throw new WTException("이미 채팅방을 생성하였습니다.");
        } else {
            Chat chatRoom = Chat.builder().clubSeq(clubSeq).build();
            chatRepository.save(chatRoom);
        }
    }
}
