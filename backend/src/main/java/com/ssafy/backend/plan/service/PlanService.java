package com.ssafy.backend.plan.service;

import com.ssafy.backend.plan.dto.request.RequestPlanDeleteDto;
import com.ssafy.backend.plan.dto.request.RequestPlanRegistDto;
import com.ssafy.backend.plan.dto.response.ResponsePlanDeleteDto;
import com.ssafy.backend.plan.dto.response.ResponsePlanDetailDto;
import com.ssafy.backend.plan.dto.response.ResponsePlanListDto;
import com.ssafy.backend.plan.dto.response.ResponsePlanRegistDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface PlanService {
    ResponseEntity<ResponsePlanRegistDto> planRegist(RequestPlanRegistDto requestPlanRegistDto, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponsePlanListDto> planList(Long clubSeq, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponsePlanDetailDto> planDetail(Long planSeq, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponsePlanDeleteDto> planDelete(RequestPlanDeleteDto requestPlanDeleteDto, HttpServletRequest httpServletRequest);
}
