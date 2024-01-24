package com.ssafy.backend.member.dto.request;

import com.ssafy.backend.member.domain.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestMemberCheckIdDto {
    private String memberId;

}
