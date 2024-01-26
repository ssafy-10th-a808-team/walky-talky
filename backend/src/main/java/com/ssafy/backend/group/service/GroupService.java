package com.ssafy.backend.group.service;

import com.ssafy.backend.group.dto.request.RequestCheckNameDto;

public interface GroupService {
    boolean checkName(RequestCheckNameDto requestCheckNameDto);
}
