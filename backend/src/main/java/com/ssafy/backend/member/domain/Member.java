package com.ssafy.backend.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "login_type")
    private String loginType;

    @Column(name = "id")
    private String memberId;

    private String password;
    private String birth;
    private String gender;
    private String nickname;
    private String url;
    private String introduce;

    @Column(name = "region_cd")
    private String regionCd;

    @Column(name = "is_alert")
    private boolean isAlert;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
