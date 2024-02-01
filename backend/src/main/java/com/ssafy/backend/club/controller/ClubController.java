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

        // null check
        if (requestClubCreateDto.isAnyFieldNull()) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("null data not allowed")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }

        // empty check
        if (requestClubCreateDto.isAnyFieldEmpty()) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("empty data now allowed")
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

        ResponseClubDetailDto responseClubDetailDto;

        if (clubSeq == null) {
            responseClubDetailDto = ResponseClubDetailDto
                    .builder()
                    .message("null data not allowed")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDetailDto);
        }


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


