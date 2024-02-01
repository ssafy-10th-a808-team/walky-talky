package com.ssafy.backend.chat.handler;

import com.ssafy.backend.chat.domain.Chat;
import com.ssafy.backend.chat.domain.dto.ChatMessageDto;
import com.ssafy.backend.chat.repository.ChatRepository;
import com.ssafy.backend.clubMember.repository.ClubMemberRepository;
import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor{

    private final JwtProvider jwtProvider;
    private final ClubMemberRepository clubMemberRepository;
    private final ChatRepository chatRepository;
    private static final List<String> ROLE = List.of("owner", "member");

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        ChatMessageDto payload = (ChatMessageDto) message.getPayload();

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String atk = accessor.getFirstNativeHeader("Authorization");
            if (!jwtProvider.validateToken(atk)) {
                throw new WTException("인증이 필요합니다.");
            }

            Long memberSeq = jwtProvider.getSeq(atk);
            Long chatSeq = payload.getChatSeq();
            Chat chat = chatRepository.findById(chatSeq).orElse(null);
            if (chat == null) {
                throw new WTException("채팅방이 존재하지 않습니다.");
            }
            Long clubSeq = chat.getClubSeq();
            if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRoleIn(clubSeq, memberSeq, ROLE)) {
                throw new WTException("소모임에 가입한 사람만 채팅방에 참여할 수 있습니다.");
            }
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
