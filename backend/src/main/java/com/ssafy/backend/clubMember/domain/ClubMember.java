package com.ssafy.backend.clubMember.domain;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.global.domain.BaseEntity;
import com.ssafy.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "club_member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "club_seq")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "member_seq")
    private Member member;

    private String role;
}
