package com.ssafy.backend.shareBoard.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.member.dto.mapping.NicknameUrlMapping;
import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.record.repository.RecordDetailRepository;
import com.ssafy.backend.record.repository.RecordRepository;
import com.ssafy.backend.record.repository.ScrapRepository;
import com.ssafy.backend.region.service.RegionService;
import com.ssafy.backend.shareBoard.domain.ShareBoard;
import com.ssafy.backend.shareBoard.domain.ShareBoardComment;
import com.ssafy.backend.shareBoard.dto.mapping.ShareBoardMemberMapping;
import com.ssafy.backend.shareBoard.dto.mapping.ShareBoardScrapMapping;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;
import com.ssafy.backend.shareBoard.dto.response.*;
import com.ssafy.backend.shareBoard.repository.ShareBoardCommentRepository;
import com.ssafy.backend.shareBoard.repository.ShareBoardLikeRepository;
import com.ssafy.backend.shareBoard.repository.ShareBoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    private final ScrapRepository scrapRepository;

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

                responseShareBoardDto.setMember(getMemberNicknameUrl(shareBoard.getMemberSeq()));

                responseShareBoardDto.setRecordSeq(shareBoard.getRecordSeq());
                responseShareBoardDto.setTitle(shareBoard.getTitle());
                responseShareBoardDto.setCreate_at(String.valueOf(shareBoard.getCreatedAt()));
                responseShareBoardDto.setHit(shareBoard.getHit());
                responseShareBoardDto.setCommentCount(shareBoardCommentRepository.countByShareBoardSeqAndIsDeletedFalse(shareBoard.getSeq()));

                list.add(responseShareBoardDto);
            } catch (Exception e) {
                throw new WTException(e.getMessage()); // Todo : 개발 끝나고 고치기
            }
        }

        return list;
    }

    @Override
    public List<ResponseLikeDto> listLike(Long memberSeq) throws WTException {
        List<ShareBoardMemberMapping> boardList = shareBoardRepository.findSeqAndMemberSeqByIsDeletedFalse();
        List<ResponseLikeDto> list = new ArrayList<>();

        ResponseLikeDto responseLikeDto = new ResponseLikeDto();
        for (ShareBoardMemberMapping shareBoardMapping : boardList) {
            try {
                Long shareBoardSeq = shareBoardMapping.getSeq();

                responseLikeDto.setShareBoardSeq(shareBoardSeq);
                responseLikeDto.setLikeCount(shareBoardLikeRepository.countAllByShareBoardSeq(shareBoardSeq));
                responseLikeDto.setLiked(shareBoardLikeRepository.existsByShareBoardSeqAndMemberSeq(shareBoardSeq, memberSeq));

                list.add(responseLikeDto);
            } catch (Exception e) {
                throw new WTException(e.getMessage()); // Todo : 바꾸기
            }
        }

        return list;
    }

    @Override
    public List<ResponseScrapDto> listScrap(Long memberSeq) throws WTException {
        List<ShareBoardScrapMapping> boardList = shareBoardRepository.findSeqAndRecordSeqByIsDeletedFalse();
        List<ResponseScrapDto> list = new ArrayList<>();

        ResponseScrapDto responseScrapDto = new ResponseScrapDto();
        for (ShareBoardScrapMapping shareBoardScrapMapping : boardList) {
            try {
                Long shareBoardSeq = shareBoardScrapMapping.getSeq();

                responseScrapDto.setShareBoardSeq(shareBoardSeq);

                Long recordSeq = shareBoardScrapMapping.getRecordSeq();
                responseScrapDto.setRecordSeq(recordSeq);
                responseScrapDto.setScraped(scrapRepository.existsByRecordSeqAndMemberSeq(recordSeq, memberSeq));

                responseScrapDto.setScrapCount(scrapRepository.countAllByRecordSeq(recordSeq));

                list.add(responseScrapDto);
            } catch (Exception e) {
                throw new WTException(e.getMessage()); // Todo : 바꾸기
            }

        }

        return list;
    }

    @Override
    @Transactional
    public ResponseShareBoardContentDto viewContent(Long shareBoardSeq) throws WTException {
        ResponseShareBoardContentDto responseShareBoardContentDto = new ResponseShareBoardContentDto();

        responseShareBoardContentDto.setShareBoardSeq(shareBoardSeq);

        int isHitUpdated = shareBoardRepository.updateHit(shareBoardSeq);
        if (isHitUpdated != 1) {
            throw new WTException("글 상세 조회 오류");
        }

        ShareBoard shareBoard = shareBoardRepository.findBySeqAndIsDeletedFalse(shareBoardSeq);
        ResponseMemberDto responseMemberDto = new ResponseMemberDto();

        try {
            responseShareBoardContentDto.setTitle(shareBoard.getTitle());
            responseShareBoardContentDto.setContent(shareBoard.getContent());

            NicknameUrlMapping m = memberRepository.findNickNameAndUrlBySeq(shareBoard.getMemberSeq());
            responseMemberDto.setNickname(m.getNickname());
            responseMemberDto.setProfilePic(m.getUrl());

            responseShareBoardContentDto.setMember(responseMemberDto);

            responseShareBoardContentDto.setRecordSeq(shareBoard.getRecordSeq());
            responseShareBoardContentDto.setCreate_at(String.valueOf(shareBoard.getCreatedAt()));
            responseShareBoardContentDto.setHit(shareBoard.getHit());
            responseShareBoardContentDto.setCommentCount(shareBoardCommentRepository.countByShareBoardSeqAndIsDeletedFalse(shareBoardSeq));
        } catch (Exception e) {
            throw new WTException(e.getMessage()); // Todo : 고치기
        }

        return responseShareBoardContentDto;
    }

    @Override
    public List<ResponseCommentDto> viewComment(Long shareBoardSeq) throws WTException {
        List<ShareBoardComment> commentDomainList = shareBoardCommentRepository.findAllByShareBoardSeqAndIsDeletedFalse(shareBoardSeq);
        List<ResponseCommentDto> list = new ArrayList<>();

        ResponseCommentDto responseCommentDto = new ResponseCommentDto();

        responseCommentDto.setShareBoardSeq(shareBoardSeq);

        for (ShareBoardComment shareBoardComment : commentDomainList) {
            try {
                responseCommentDto.setContent(shareBoardComment.getContent());
                responseCommentDto.setMember(getMemberNicknameUrl(shareBoardComment.getMemberSeq()));
                responseCommentDto.setCreated_at(shareBoardComment.getCreatedBy());

                list.add(responseCommentDto);
            } catch (Exception e) {
                throw new WTException(e.getMessage()); // Todo : 고치키
            }
        }

        return list;
    }

    public ResponseMemberDto getMemberNicknameUrl(Long memberSeq) {
        ResponseMemberDto responseMemberDto = new ResponseMemberDto();

        NicknameUrlMapping m = memberRepository.findNickNameAndUrlBySeq(memberSeq);
        responseMemberDto.setNickname(m.getNickname());
        responseMemberDto.setProfilePic(m.getUrl());

        return responseMemberDto;
    }
}
