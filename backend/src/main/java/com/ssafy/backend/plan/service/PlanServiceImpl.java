package com.ssafy.backend.plan.service;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.club.repository.ClubRepository;
import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.clubMember.repository.ClubMemberRepository;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.plan.domain.Plan;
import com.ssafy.backend.plan.dto.request.RequestPlanDeleteDto;
import com.ssafy.backend.plan.dto.request.RequestPlanRegistDto;
import com.ssafy.backend.plan.dto.response.*;
import com.ssafy.backend.plan.repository.PlanRepository;
import com.ssafy.backend.planClubMember.domain.PlanClubMember;
import com.ssafy.backend.planClubMember.repository.PlanClubMemberRepository;
import com.ssafy.backend.record.domain.Record;
import com.ssafy.backend.record.repository.RecordDetailRepository;
import com.ssafy.backend.record.repository.RecordRepository;
import com.ssafy.backend.region.repository.RegionRepository;
import com.ssafy.backend.region.service.RegionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final ClubRepository clubRepository;
    private final RecordRepository recordRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final PlanClubMemberRepository planClubMemberRepository;
    private final RecordDetailRepository recordDetailRepository;
    private final RegionService regionService;

    @Override
    public ResponseEntity<ResponsePlanRegistDto> planRegist(RequestPlanRegistDto requestPlanRegistDto, HttpServletRequest httpServletRequest) {
        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");
        ResponsePlanRegistDto presResponsePlanRegistDto;

        // 일정을 보고 만들었으므로 이미 club에 유효한 사용자이다

        Club findedClub = clubRepository.findById(requestPlanRegistDto.getClubSeq()).orElse(null);

        Record findedRecord = null;
        if (requestPlanRegistDto.getRecordSeq() != null)
            findedRecord = recordRepository.findById(requestPlanRegistDto.getRecordSeq()).orElse(null);

        ClubMember findedClubMember = clubMemberRepository.findByMemberSeqAndClubSeq(myMemberSeq, requestPlanRegistDto.getClubSeq());

        Plan plan = Plan.builder()
                .seq(null)
                .club(findedClub)
                .record(findedRecord)
                .clubMember(findedClubMember)
                .title(requestPlanRegistDto.getTitle())
                .content(requestPlanRegistDto.getContent())
                .startTime(requestPlanRegistDto.getStartTime())
                .duration(requestPlanRegistDto.getDuration())
                .longitude(requestPlanRegistDto.getLongitude())
                .latitude(requestPlanRegistDto.getLatitude())
                .nowCapacity(1)
                .maxCapacity(requestPlanRegistDto.getMaxCapacity())
                .build();

        planRepository.save(plan);

        PlanClubMember planClubMember = PlanClubMember.builder()
                .plan(plan)
                .clubMember(findedClubMember)
                .build();
        planClubMemberRepository.save(planClubMember);


        presResponsePlanRegistDto = ResponsePlanRegistDto.builder()
                .message("OK")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(presResponsePlanRegistDto);

    }

    @Override
    public ResponseEntity<ResponsePlanListDto> planList(Long clubSeq, HttpServletRequest httpServletRequest) {
        ResponsePlanListDto responsePlanListDto = new ResponsePlanListDto();

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");

        // 해당 모임원이 아니면 내쫓아
        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRoleIn(clubSeq, myMemberSeq, Arrays.asList("owner", "member"))) {
            responsePlanListDto = ResponsePlanListDto.builder()
                    .message("해당 모임원이 아닙니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responsePlanListDto);
        }

        List<Plan> findedPlans = planRepository.findAllByClubSeq(clubSeq);

        responsePlanListDto.setPlans(new ArrayList<>());

        for (Plan findedPlan : findedPlans) {

            ResponsePlanListDtoPlan responsePlanListDtoPlan = ResponsePlanListDtoPlan.builder()
                    .seq(findedPlan.getSeq())
                    .clubSeq(findedPlan.getClub().getSeq())
//                            .recordSeq(findedPlan.getRecord().getSeq())
                    .clubMemberSeq(findedPlan.getClubMember().getSeq())
                    .title(findedPlan.getTitle())
                    .content(findedPlan.getContent())
                    .startTime(findedPlan.getStartTime())
                    .duration(findedPlan.getDuration())
                    .latitude(findedPlan.getLatitude())
                    .longitude(findedPlan.getLongitude())
                    .nowCapacity(findedPlan.getNowCapacity())
                    .maxCapacity(findedPlan.getMaxCapacity())
                    .build();

            if (findedPlan.getRecord() != null)
                responsePlanListDtoPlan.setRecordSeq(findedPlan.getRecord().getSeq());

            responsePlanListDto.getPlans().add(responsePlanListDtoPlan);
        }

        responsePlanListDto.setMessage("OK");

        return ResponseEntity.status(HttpStatus.OK).body(responsePlanListDto);
    }

    @Override
    public ResponseEntity<ResponsePlanDetailDto> planDetail(Long planSeq, HttpServletRequest httpServletRequest) {
        ResponsePlanDetailDto responsePlanDetailDto = new ResponsePlanDetailDto();

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");

        Plan findedPlan = planRepository.findById(planSeq).orElse(null);
        // 잘못된 일정번호 입니다.
        if (findedPlan == null) {
            responsePlanDetailDto = ResponsePlanDetailDto.builder()
                    .message("잘못된 일정번호 입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responsePlanDetailDto);
        }

        ResponsePlanDetailDtoPlan responsePlanDetailDtoPlan = ResponsePlanDetailDtoPlan.builder()
                .seq(findedPlan.getSeq())
                .clubSeq(findedPlan.getClub().getSeq())
//                        .recordSeq(findedPlan.getRecord().getSeq())
                .clubMemberSeq(findedPlan.getClubMember().getSeq())
                .title(findedPlan.getTitle())
                .content(findedPlan.getContent())
                .startTime(findedPlan.getStartTime())
                .duration(findedPlan.getDuration())
                .latitude(findedPlan.getLatitude())
                .longitude(findedPlan.getLongitude())
                .nowCapacity(findedPlan.getNowCapacity())
                .maxCapacity(findedPlan.getMaxCapacity())
                .build();

        if (findedPlan.getRecord() != null)
            responsePlanDetailDtoPlan.setRecordSeq((findedPlan.getRecord().getSeq()));


        responsePlanDetailDto.setResponsePlanDetailDtoPlan(responsePlanDetailDtoPlan);

        // 일정에 기록이 없을 경우
        if (findedPlan.getRecord() == null) {
        } else {
            responsePlanDetailDto.setRecord(findedPlan.getRecord());

            responsePlanDetailDto.setRecordDetails(recordDetailRepository.findAllByRecordSeq(findedPlan.getRecord().getSeq()));
        }

        responsePlanDetailDto.setResponsePlanDetailDtoMembers(new ArrayList<>());

        List<PlanClubMember> planClubMembrs = planClubMemberRepository.findAllByPlanSeq(planSeq);
        for (PlanClubMember planClubMember : planClubMembrs) {

            Member tmpMember = planClubMember.getClubMember().getMember();

            ResponsePlanDetailDtoMember responsePlanDetailDtoMember = ResponsePlanDetailDtoMember.builder()
                    .seq(tmpMember.getSeq())
                    .birth(tmpMember.getBirth())
                    .gender(tmpMember.getGender())
                    .introduce(tmpMember.getIntroduce())
                    .nickname(tmpMember.getNickname())
                    .address(tmpMember.getUrl()).build();

            responsePlanDetailDtoMember.setAddress(regionService.findAddress(tmpMember.getRegionCd()));

            responsePlanDetailDto.getResponsePlanDetailDtoMembers().add(responsePlanDetailDtoMember);
        }


        responsePlanDetailDto.setMessage("OK");
        return ResponseEntity.status(HttpStatus.OK).body(responsePlanDetailDto);

    }

    @Override
    @Transactional
    public ResponseEntity<ResponsePlanDeleteDto> planDelete(RequestPlanDeleteDto requestPlanDeleteDto, HttpServletRequest httpServletRequest) {
        ResponsePlanDeleteDto responsePlanDeleteDto;

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long planSeq = requestPlanDeleteDto.getPlanSeq();

        // 일정을 만든 사람이 아닙니다.
        if (planRepository.findById(planSeq).orElse(null).getClubMember().getMember().getSeq() != myMemberSeq) {
            responsePlanDeleteDto = ResponsePlanDeleteDto.builder()
                    .message("일정을 만든 사람이 아닙니다.").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responsePlanDeleteDto);
        }

        planClubMemberRepository.deleteAllByPlanSeq(planSeq);
        planRepository.deleteById(planSeq);

        responsePlanDeleteDto = ResponsePlanDeleteDto.builder()
                .message("OK").build();
        return ResponseEntity.status(HttpStatus.OK).body(responsePlanDeleteDto);
    }
}
