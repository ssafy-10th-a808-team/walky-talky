package com.ssafy.backend.plan.dto.response;

import com.ssafy.backend.plan.domain.Plan;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePlanListDto {
    private String message;
    List<ResponsePlanListDtoPlan> plans;
}
