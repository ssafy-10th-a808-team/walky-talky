package com.ssafy.backend.club.dto.response;

import com.ssafy.backend.club.domain.Club;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseClubDetailDto {

    private String message;

    private Club club;

    private List<ResponseClubDetailDtoMember> members;
}
