package com.ssafy.backend.club.service;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.club.dto.request.RequestCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.dto.request.ResponseClubListDto;
import jakarta.servlet.http.HttpServletRequest;

public interface ClubService {
    boolean checkName(RequestCheckNameDto requestCheckNameDto);

    void clubCreate(RequestClubCreateDto requestClubCreateDto, HttpServletRequest httpServletRequest);

    ResponseClubListDto clubList(HttpServletRequest httpServletRequest);
}
