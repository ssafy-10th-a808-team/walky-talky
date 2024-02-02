package com.ssafy.backend.plan.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePlanDetailDtoMember {
    private Long seq;
    private String birth;
    private String gender;
    private String introduce;
    private String nickname;
    private String address;
    private String url;
}
