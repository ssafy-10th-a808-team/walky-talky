package com.ssafy.backend.member.dto.request;

import com.ssafy.backend.member.domain.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
//    private MultipartFile profileImg;
    private String profileImg;
    private String introduce;
    private String address;

    public Member toEntity() {
        return Member.builder()
                .seq(null)
                .loginType("0")
                .memberId(id)
                .password(password)
                .birth(birth)
                .gender(gender)
                .nickname(nickname)
                .url("바꿔야함")
                .introduce(introduce)
                .regionCd("바꿔야함")
                .isAlert(true)
                .isDeleted(false)
                .build();
    }
}
