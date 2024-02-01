package com.ssafy.backend.club.controller;

import com.ssafy.backend.club.dto.request.RequestClubCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.dto.request.RequestClubDeleteDto;
import com.ssafy.backend.club.dto.request.RequestClubModifyDto;
import com.ssafy.backend.club.dto.response.*;
import com.ssafy.backend.club.service.ClubService;
import com.ssafy.backend.member.dto.response.ResponseCheckNicknameDto;
import com.ssafy.backend.member.dto.response.ResponseLocalSignupDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/club")
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/check-name")
    public ResponseEntity<ResponseClubCheckNameDto> clubCheckName(@RequestBody RequestClubCheckNameDto requestClubCheckNameDto) {

        ResponseClubCheckNameDto responseClubCheckNameDto;

        if (requestClubCheckNameDto.getName() == null) {
            responseClubCheckNameDto = ResponseClubCheckNameDto.builder()
                    .message("name is null")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCheckNameDto);
        }

        if (requestClubCheckNameDto.getName().isEmpty()) {
            responseClubCheckNameDto = ResponseClubCheckNameDto.builder()
                    .message("name is empty")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCheckNameDto);
        }

        return clubService.clubCheckName(requestClubCheckNameDto);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseClubCreateDto> clubCreate(
            RequestClubCreateDto requestClubCreateDto,
            HttpServletRequest httpServletRequest) throws IOException {

        ResponseClubCreateDto responseClubCreateDto;

        //        private MultipartFile multipartFile;
//        private String name;
//        private String introduce;
//        private String regionCd;
//        private String young_birth;
//        private String old_birth;
//        private String gender_type;
//        private int max_capacity;
//        private Boolean is_auto_recruit;

        // TODO : null check

        // empty check
        if (requestClubCreateDto.getName().isEmpty()) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("name is empty")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }
        if (requestClubCreateDto.getRegionCd().isEmpty()) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("regionCd is empty")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }
        if (requestClubCreateDto.getYoung_birth().isEmpty()) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("yong_birth is empty")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }
        if (requestClubCreateDto.getOld_birth().isEmpty()) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("old_birth is empty")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }
        if (requestClubCreateDto.getGender_type().isEmpty()) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("gender_type is empty")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }

        return clubService.clubCreate(requestClubCreateDto, httpServletRequest);
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseClubListDto> clubList(HttpServletRequest httpServletRequest) {
        return clubService.clubList(httpServletRequest);
    }

    @GetMapping("/detail")
    public ResponseEntity<ResponseClubDetailDto> clubDetail(@RequestParam("clubSeq") Long clubSeq) {
        return clubService.clubDetail(clubSeq);
    }

    @PostMapping("/modify")
    public ResponseEntity<ResponseClubModifyDto> clubModify(
            RequestClubModifyDto requestClubModifyDto,
            HttpServletRequest httpServletRequest) throws IOException {
        return clubService.clubModify(requestClubModifyDto, httpServletRequest);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseClubDeleteDto> clubDelete(
            @RequestBody RequestClubDeleteDto requestClubDeleteDto,
            HttpServletRequest httpServletRequest) {

        return clubService.clubDelete(requestClubDeleteDto, httpServletRequest);

    }

}


