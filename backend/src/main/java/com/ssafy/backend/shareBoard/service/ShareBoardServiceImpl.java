package com.ssafy.backend.shareBoard.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.record.repository.RecordDetailRepository;
import com.ssafy.backend.record.repository.RecordRepository;
import com.ssafy.backend.region.service.RegionService;
import com.ssafy.backend.shareBoard.domain.ShareBoard;
import com.ssafy.backend.shareBoard.dto.mapping.ShareBoardMemberMapping;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;
import com.ssafy.backend.shareBoard.dto.response.ResponseMemberDto;
import com.ssafy.backend.shareBoard.dto.response.ResponseShareBoardDto;
import com.ssafy.backend.shareBoard.repository.ShareBoardCommentRepository;
import com.ssafy.backend.shareBoard.repository.ShareBoardLikeRepository;
import com.ssafy.backend.shareBoard.repository.ShareBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShareBoardServiceImpl implements ShareBoardService {

    private final ShareBoardRepository shareBoardRepository;

    private final ShareBoardCommentRepository shareBoardCommentRepository;

    private final ShareBoardLikeRepository shareBoardLikeRepository;

    private final MemberRepository memberRepository;

    private final RecordRepository recordRepository;

    private final RecordDetailRepository recordDetailRepository;

    private final RegionService regionService;

    @Override
    public void write(Long memberSeq, RequestShareBoardWriteDto requestShareBoardWriteDto) throws WTException {
        try {
            ShareBoard shareBoard = ShareBoard.builder()
                    .memberSeq(memberSeq)
                    .recordSeq(requestShareBoardWriteDto.getRecordSeq())
                    .title(requestShareBoardWriteDto.getTitle())
                    .content(requestShareBoardWriteDto.getContent())
                    .build();

            shareBoardRepository.save(shareBoard);
        } catch (Exception e) {
            throw new WTException(e.getMessage()); // Todo : 개발 끝나고 고치기
        }
    }

    @Override
    public List<ResponseShareBoardDto> listContent() throws WTException {
        List<ShareBoard> boardList = shareBoardRepository.findAllByIsDeletedFalse();
        List<ResponseShareBoardDto> list = new ArrayList<>();

        ResponseShareBoardDto responseShareBoardDto = new ResponseShareBoardDto();

        for (ShareBoard shareBoard : boardList) {
            try {
                responseShareBoardDto.setShareBoardSeq(shareBoard.getSeq());
                responseShareBoardDto.setRecordSeq(shareBoard.getRecordSeq());
                responseShareBoardDto.setTitle(shareBoard.getTitle());
                responseShareBoardDto.setCreate_at(String.valueOf(shareBoard.getCreatedAt()));
                responseShareBoardDto.setHit(shareBoard.getHit());
                responseShareBoardDto.setCommentCount(Math.toIntExact(shareBoardCommentRepository.countByShareBoardSeqAndIsDeletedFalse(shareBoard.getSeq())));

                list.add(responseShareBoardDto);
            } catch (Exception e) {
                throw new WTException(e.getMessage()); // Todo : 개발 끝나고 고치기
            }
        }

        return list;
    }

    @Override
    public List<ResponseMemberDto> listMember() throws WTException {
        List<ShareBoardMemberMapping> boardList = shareBoardRepository.findSeqAndMemberSeqByIsDeletedFalse();
        List<ResponseMemberDto> list = new ArrayList<>();

        ResponseMemberDto responseMemberDto = new ResponseMemberDto();
        for(ShareBoardMemberMapping shareBoardMapping:boardList){
            try{
                Long shareBoardSeq = shareBoardMapping.getSeq();
                responseMemberDto.setShareBoardSeq(shareBoardSeq);

                Optional<Member> m = memberRepository.findById(shareBoardMapping.getMemberSeq());
                if(m.isEmpty()){
                    throw new WTException("멤버 null"); // Todo : 나중에 고치기
                }

                Member member = m.get();
                responseMemberDto.setNickname(member.getNickname());
                responseMemberDto.setProfilePic(member.getUrl());

                list.add(responseMemberDto);
            }catch (Exception e){
                throw new WTException(e.getMessage()); // Todo : 고치기
            }
        }

        return list;
    }
}
