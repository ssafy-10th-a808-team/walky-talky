package com.ssafy.backend.member.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestCheckNicknameDto {
    private String nickname;
}
