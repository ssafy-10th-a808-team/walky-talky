package com.ssafy.backend.chat.handler;

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
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor{

    private final JwtProvider jwtProvider;
    private final ClubMemberRepository clubMemberRepository;
    private static final List<String> ROLE = List.of("owner", "member");

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String atk = accessor.getFirstNativeHeader("atk");

        if (StompCommand.CONNECT == accessor.getCommand() || StompCommand.SEND == accessor.getCommand()) {
            atk = atk.substring("Bearer ".length());
            if (!jwtProvider.validateToken(atk)) {
                throw new WTException("인증이 필요합니다.");
            }
            Long clubSeq = Long.parseLong(Objects.requireNonNull(accessor.getFirstNativeHeader("clubSeq")));
            Long memberSeq = jwtProvider.getSeq(atk);

            if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRoleIn(clubSeq, memberSeq, ROLE)) {
                throw new WTException("소모임에 가입해주세요.");
            }
        }
        return message;
    }


}
