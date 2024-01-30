package com.ssafy.backend.clubMember.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class RequestClubMemberApplyAcceptDto {
    private Long clubSeq;
    private Long memberSeq;
}
