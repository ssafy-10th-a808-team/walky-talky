package com.ssafy.backend.chat.service;

import com.ssafy.backend.chat.domain.Chat;
import com.ssafy.backend.member.domain.Member;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.List;

public interface ChatService {
    void createChatRoom(Long clubSeq);
    ChannelTopic getTopic(Long clubSeq);
    void enterChatRoom(Long clubSeq);
}
