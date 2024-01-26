package com.ssafy.backend.group.service;

import com.ssafy.backend.group.dto.request.RequestCheckNameDto;
import com.ssafy.backend.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    public boolean checkName(RequestCheckNameDto requestCheckNameDto) {

    }
}
