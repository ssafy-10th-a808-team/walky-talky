package com.ssafy.backend.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestModifyInfoDto {
    String regionCd;
    String introduce;
    String nickname;
    MultipartFile profileImage;

    @Override
    public String toString() {
        return "RequestModifyInfoDto{" +
                "regionCd='" + regionCd + '\'' +
                ", introduce='" + introduce + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profileImage=" + profileImage +
                '}';
    }
}
