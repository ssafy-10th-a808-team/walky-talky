package com.ssafy.backend.club.service;

import com.ssafy.backend.club.dto.request.RequestCheckNameDto;

public interface ClubService {
    boolean checkName(RequestCheckNameDto requestCheckNameDto);
}
