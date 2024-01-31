package com.ssafy.backend.clubMember.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestClubMemberApplyRejectDto {
    private Long clubSeq;
    private Long memberSeq;

}
