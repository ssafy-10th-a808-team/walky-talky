package com.ssafy.backend.chat.domain;

import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long chatSeq;

//    @ManyToOne
//    @JoinColumn(name = "clubmember_seq")
//    private ClubMember clubMember;

    private Long memberSeq;
    private String content;
    private MessageType type;
    public enum MessageType {
        ENTER, TALK, EXIT
    }
}
