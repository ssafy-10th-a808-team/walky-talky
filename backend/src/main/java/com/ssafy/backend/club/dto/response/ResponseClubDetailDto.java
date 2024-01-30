package com.ssafy.backend.club.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseClubDetailDto {

    private String message;
    private ResponseClubDetailDtoClub responseClubDetailDtoClub;
    private List<ResponseClubDetailDtoMember> responseClubDetailDtoMembers;
}
