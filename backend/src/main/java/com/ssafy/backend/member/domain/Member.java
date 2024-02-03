package com.ssafy.backend.member.domain;

import com.ssafy.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private Boolean isAlert;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public Member update(String regionCd, String introduce, String nickname, String url) {
        this.regionCd = regionCd;
        this.introduce = introduce;
        this.nickname = nickname;
        this.url = url;
        return this;
    }

    public Member update(String regionCd, String introduce, String nickname) {
        this.regionCd = regionCd;
        this.introduce = introduce;
        this.nickname = nickname;
        return this;
    }

    public Member update(String password) {
        this.password = password;
        return this;
    }

    public Member delete(){
        this.isDeleted = true;
        return this;
    }

}
