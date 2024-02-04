package com.ssafy.backend.planClubMember.service;

import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.clubMember.repository.ClubMemberRepository;
import com.ssafy.backend.plan.domain.Plan;
import com.ssafy.backend.plan.repository.PlanRepository;
import com.ssafy.backend.planClubMember.domain.PlanClubMember;
import com.ssafy.backend.planClubMember.dto.request.RequestPlanClubMemberApplyDto;
import com.ssafy.backend.planClubMember.dto.request.RequestPlanClubMemberCancelDto;
import com.ssafy.backend.planClubMember.dto.response.ResponsePlanClubMemberApplyDto;
import com.ssafy.backend.planClubMember.dto.response.ResponsePlanClubMemberCancelDto;
import com.ssafy.backend.planClubMember.repository.PlanClubMemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanClubMemberServiceImpl implements PlanClubMemberService {

    private final PlanRepository planRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final PlanClubMemberRepository planClubMemberRepository;

    @Override
    @Transactional
    public ResponseEntity<ResponsePlanClubMemberApplyDto> planClubMemberApply(RequestPlanClubMemberApplyDto requestPlanClubMemberApplyDto, HttpServletRequest httpServletRequest) {
        ResponsePlanClubMemberApplyDto responsePlanClubMemberApplyDto;

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long planSeq = requestPlanClubMemberApplyDto.getPlanSeq();

        System.out.println("myMemberSeq = " + myMemberSeq);
        System.out.println("planSeq = " + planSeq);

        // Plan 인원 추가
        Plan findedPlan = planRepository.findById(planSeq).orElse(null);
        findedPlan.setNowCapacity(findedPlan.getNowCapacity() + 1);
        planRepository.save(findedPlan);

        // PlanClubMember 추가
        Long clubSeq = findedPlan.getClub().getSeq();
        System.out.println("clubSeq = " + clubSeq);

        ClubMember clubMember = clubMemberRepository.findByClubSeqAndMemberSeq(clubSeq, myMemberSeq);

        if (clubMember == null) {
            System.out.println("clubMember를 찾지 못했습니다.");
        }

        PlanClubMember planClubMember = PlanClubMember.builder()
                .clubMember(clubMember)
                .plan(findedPlan)
                .build();

        planClubMemberRepository.save(planClubMember);


        responsePlanClubMemberApplyDto = ResponsePlanClubMemberApplyDto.builder().message("OK").build();
        return ResponseEntity.status(HttpStatus.OK).body(responsePlanClubMemberApplyDto);


    }

    @Override
    @Transactional
    public ResponseEntity<ResponsePlanClubMemberCancelDto> planClubMemberCancel(RequestPlanClubMemberCancelDto requestPlanClubMemberCancelDto, HttpServletRequest httpServletRequest) {
        ResponsePlanClubMemberCancelDto responsePlanClubMemberCancelDto;

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long planSeq = requestPlanClubMemberCancelDto.getPlanSeq();

        System.out.println("myMemberSeq = " + myMemberSeq);
        System.out.println("planSeq = " + planSeq);

        // Plan 인원 낮추기
        Plan findedPlan = planRepository.findById(planSeq).orElse(null);
        findedPlan.setNowCapacity(findedPlan.getNowCapacity() - 1);
        planRepository.save(findedPlan);

        // PlanClubMember 제거
        Long clubSeq = findedPlan.getClub().getSeq();
        System.out.println("clubSeq = " + clubSeq);

        ClubMember clubMember = clubMemberRepository.findByClubSeqAndMemberSeq(clubSeq, myMemberSeq);

        if (clubMember == null) {
            System.out.println("clubMember를 찾지 못했습니다.");
        }

        planClubMemberRepository.deleteByClubMemberSeqAndPlanSeq(clubMember.getSeq(), planSeq);


        responsePlanClubMemberCancelDto = ResponsePlanClubMemberCancelDto.builder().message("OK").build();
        return ResponseEntity.status(HttpStatus.OK).body(responsePlanClubMemberCancelDto);

    }
}
