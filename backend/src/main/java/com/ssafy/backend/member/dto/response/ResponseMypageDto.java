package com.ssafy.backend.member.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMypageDto {
    String nickname;
    String birth;
    String gender;
    String memberId;
    String address;
    String regionCd;
    String introduce;
    String profileImage;
}
