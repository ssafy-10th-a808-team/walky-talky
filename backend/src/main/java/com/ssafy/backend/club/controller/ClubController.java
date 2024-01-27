package com.ssafy.backend.club.controller;

import com.ssafy.backend.club.dto.request.RequestCheckNameDto;
import com.ssafy.backend.club.dto.response.ResponseCheckNameDto;
import com.ssafy.backend.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/club")
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/check-name")
    public ResponseEntity<ResponseCheckNameDto> checkName(@RequestBody RequestCheckNameDto requestCheckNameDto) {

        ResponseCheckNameDto responseCheckNameDto = new ResponseCheckNameDto();

        if (clubService.checkName(requestCheckNameDto)) {
            responseCheckNameDto.setMessage("이미 존재하는 소모임 명입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCheckNameDto);
        } else {
            responseCheckNameDto.setMessage("OK");
            return ResponseEntity.status(HttpStatus.OK).body(responseCheckNameDto);
        }
    }
}


