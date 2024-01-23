package com.ssafy.backend.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "login_type")
    private String loginType;

    @Column(name = "member_id")
    private String memberId;

    private String password;
    private String birth;
    private String gender;
    private String nickname;
    private String url;
    private String introduce;
    private String regionCd;
    private boolean isAlert = true;
    private boolean isDeleted = false;
}
