package com.ssafy.backend.member.dto.response;

import com.ssafy.backend.member.domain.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseLocalSignupDto {
    private String message;

}
