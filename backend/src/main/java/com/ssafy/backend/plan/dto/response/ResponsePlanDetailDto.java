package com.ssafy.backend.plan.dto.response;

import com.ssafy.backend.plan.domain.Plan;
import com.ssafy.backend.record.domain.Record;
import com.ssafy.backend.record.domain.RecordDetail;
import com.ssafy.backend.record.dto.mapping.PointsMapping;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePlanDetailDto {
    private String message;
    private ResponsePlanDetailDtoPlan responsePlanDetailDtoPlan;
    private Record record;
    private List<PointsMapping> recordDetails;
    private List<ResponsePlanDetailDtoMember> responsePlanDetailDtoMembers;

}
