package com.ssafy.backend.member.dto.request;

import com.ssafy.backend.member.domain.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLocalSignupDto {

    private String id;
    private String password;
    private String birth;
    private String gender;
    private String nickname;
    private String introduce;
    private String regionCd;

    public Member toEntity() {
        return Member.builder()
                .seq(null)
                .loginType("0")
                .memberId(id)
                .password(password)
                .birth(birth)
                .gender(gender)
                .nickname(nickname)
                .url(null)
                .introduce(introduce)
                .regionCd(regionCd)
                .isAlert(true)
                .isDeleted(false)
                .build();
    }
}
