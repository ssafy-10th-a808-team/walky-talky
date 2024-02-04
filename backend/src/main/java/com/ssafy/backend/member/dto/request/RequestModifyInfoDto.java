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
    private String regionCd;
    private String introduce;
    private String nickname;
    private MultipartFile profileImage;
}
