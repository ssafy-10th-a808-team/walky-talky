package com.ssafy.backend.plan.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestPlanOverwriteDto {
    private Long planSeq;
    private Long recordSeq;
}
