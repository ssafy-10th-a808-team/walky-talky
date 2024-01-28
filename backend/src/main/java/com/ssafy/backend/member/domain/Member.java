package com.ssafy.backend.member.domain;

import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "login_type")
    private String loginType;

    @Column(name = "id")
    @NotNull
    private String memberId;

    private String password;
    private String birth;
    private String gender;
    private String nickname;

    @Column(length = 2083)
    private String url;
    private String introduce;

    @Column(name = "region_cd")
    private String regionCd;

    @Column(name = "is_alert")
    private boolean isAlert;

    @Column(name = "is_deleted")
    private boolean isDeleted;

}
