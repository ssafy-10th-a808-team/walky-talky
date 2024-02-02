package com.ssafy.backend.plan.controller;

import com.ssafy.backend.plan.dto.request.RequestPlanDeleteDto;
import com.ssafy.backend.plan.dto.request.RequestPlanRegistDto;
import com.ssafy.backend.plan.dto.response.ResponsePlanDeleteDto;
import com.ssafy.backend.plan.dto.response.ResponsePlanDetailDto;
import com.ssafy.backend.plan.dto.response.ResponsePlanListDto;
import com.ssafy.backend.plan.dto.response.ResponsePlanRegistDto;
import com.ssafy.backend.plan.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/plan")
public class PlanController {

    private final PlanService planService;

    @GetMapping("/test")
    public ResponseEntity<?> planTest(HttpServletRequest httpServletRequest) {
        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");

        System.out.println("api/plan/test checkPoint1");

        System.out.println("myMemberSeq = " + myMemberSeq);

        return ResponseEntity.status(HttpStatus.OK).body("myMemberSeq = " + myMemberSeq);
    }

    @PostMapping("/regist")
    public ResponseEntity<ResponsePlanRegistDto> planRegist(@RequestBody RequestPlanRegistDto requestPlanRegistDto, HttpServletRequest httpServletRequest) {
        return planService.planRegist(requestPlanRegistDto, httpServletRequest);
    }

    @GetMapping("/{clubSeq}/list")
    public ResponseEntity<ResponsePlanListDto> planList(@PathVariable("clubSeq") Long clubSeq, HttpServletRequest httpServletRequest) {
        return planService.planList(clubSeq, httpServletRequest);
    }

    @GetMapping("/{planSeq}/detail")
    public ResponseEntity<ResponsePlanDetailDto> planDetail(@PathVariable("planSeq") Long planSeq, HttpServletRequest httpServletRequest) {
        return planService.planDetail(planSeq, httpServletRequest);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponsePlanDeleteDto> planDelete(@RequestBody RequestPlanDeleteDto requestPlanDeleteDto, HttpServletRequest httpServletRequest) {
        return planService.planDelete(requestPlanDeleteDto, httpServletRequest);
    }

}
