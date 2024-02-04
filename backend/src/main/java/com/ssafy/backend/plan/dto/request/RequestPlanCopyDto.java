package com.ssafy.backend.plan.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestPlanCopyDto {
    private Long clubSeq;
    private Long recordSeq;
}
