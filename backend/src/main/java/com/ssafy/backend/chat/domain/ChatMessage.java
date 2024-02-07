package com.ssafy.backend.chat.domain;

import com.ssafy.backend.chat.domain.dto.MessageType;
import com.ssafy.backend.clubMember.domain.ClubMember;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "club_chat_message")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_member_seq")
    private ClubMember clubMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_chat_seq")
    private Chat chat;
    private Long clubSeq;
    private String sender;
    private String content;
    private String createdAt;
    private MessageType type;
}
