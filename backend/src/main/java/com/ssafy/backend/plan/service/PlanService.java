package com.ssafy.backend.plan.service;

import com.ssafy.backend.plan.dto.request.*;
import com.ssafy.backend.plan.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface PlanService {
    ResponseEntity<ResponsePlanRegistDto> planRegist(RequestPlanRegistDto requestPlanRegistDto, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponsePlanListDto> planList(Long clubSeq, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponsePlanDetailDto> planDetail(Long planSeq, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponsePlanDeleteDto> planDelete(RequestPlanDeleteDto requestPlanDeleteDto, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponsePlanModifyDto> planModify(RequestPlanModifyDto requestPlanModifyDto, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponsePlanOverwriteDto> planOverwrite(RequestPlanOverwriteDto requestPlanOverwriteDto, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponsePlanCopyDto> planCopy(RequestPlanCopyDto requestPlanCopyDto, HttpServletRequest httpServletRequest);
}
