package com.ssafy.backend.member.dto.request;

import com.ssafy.backend.member.domain.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestLocalSignupDto {

    private MultipartFile multipartFile;
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
                .loginType("local")
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
