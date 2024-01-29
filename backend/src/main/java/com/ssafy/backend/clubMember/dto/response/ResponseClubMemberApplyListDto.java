package com.ssafy.backend.clubMember.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseClubMemberApplyListDto {
    private String message;

    List<ResponseClubMemberApplyListDtoMember> responseClubMemberApplyListDtoMembers;
}
