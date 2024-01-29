package com.ssafy.backend.club.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseClubDetailDtoMember {
    private String nickname;
    private String url;
    private String address;
    private String role;
}