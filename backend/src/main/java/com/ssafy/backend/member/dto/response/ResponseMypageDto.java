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

    @Override
    public String toString() {
        return "ResponseMypageDto{" +
                "nickname='" + nickname + '\'' +
                ", birth='" + birth + '\'' +
                ", gender='" + gender + '\'' +
                ", memberId='" + memberId + '\'' +
                ", address='" + address + '\'' +
                ", regionCd='" + regionCd + '\'' +
                ", introduce='" + introduce + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
