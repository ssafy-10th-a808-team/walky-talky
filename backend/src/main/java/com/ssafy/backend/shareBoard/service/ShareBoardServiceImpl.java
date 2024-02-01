package com.ssafy.backend.shareBoard.service;


import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.member.dto.mapping.NicknameUrlMapping;
import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.record.dto.response.ResponseViewDto;
import com.ssafy.backend.record.repository.ScrapRepository;
import com.ssafy.backend.record.service.RecordService;
import com.ssafy.backend.shareBoard.domain.ShareBoard;
import com.ssafy.backend.shareBoard.domain.ShareBoardLike;
import com.ssafy.backend.shareBoard.dto.mapping.ShareBoardMemberMapping;
import com.ssafy.backend.shareBoard.dto.mapping.ShareBoardScrapMapping;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardModifyDto;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;
import com.ssafy.backend.shareBoard.dto.response.*;
import com.ssafy.backend.shareBoard.repository.ShareBoardLikeRepository;
import com.ssafy.backend.shareBoard.repository.ShareBoardRepository;
import com.ssafy.backend.shareBoardCommet.domain.ShareBoardComment;
import com.ssafy.backend.shareBoardCommet.dto.response.ResponseCommentDto;
import com.ssafy.backend.shareBoardCommet.repository.ShareBoardCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ShareBoardServiceImpl implements ShareBoardService {

    private final ShareBoardRepository shareBoardRepository;

    private final ShareBoardCommentRepository shareBoardCommentRepository;

    private final ShareBoardLikeRepository shareBoardLikeRepository;

    private final MemberRepository memberRepository;

    private final RecordService recordService;

    private final ScrapRepository scrapRepository;

    @Override
    public void write(Long memberSeq, RequestShareBoardWriteDto requestShareBoardWriteDto) throws WTException {
        // Todo : 자기 기록으로만 글 쓸 수 있게 하기
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

        for (ShareBoard shareBoard : boardList) {
            try {
                ResponseShareBoardDto responseShareBoardDto = new ResponseShareBoardDto();

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
    public List<ResponseRecordDto> listRecord() throws WTException {
        List<ShareBoardMemberMapping> boardList = shareBoardRepository.findSeqAndMemberSeqByIsDeletedFalse();
        List<ResponseRecordDto> list = new ArrayList<>();

        for (ShareBoardMemberMapping shareBoardMapping : boardList) {
            try {
                Long shareBoardSeq = shareBoardMapping.getSeq();
                list.add(viewRecord(shareBoardSeq));
            } catch (Exception e) {
                throw new WTException(e.getMessage()); // Todo : 바꾸기
            }
        }

        return list;
    }

    @Override
    public List<ResponseLikeDto> listLike(Long memberSeq) throws WTException {
        List<ShareBoardMemberMapping> boardList = shareBoardRepository.findSeqAndMemberSeqByIsDeletedFalse();
        List<ResponseLikeDto> list = new ArrayList<>();

        for (ShareBoardMemberMapping shareBoardMapping : boardList) {
            try {
                Long shareBoardSeq = shareBoardMapping.getSeq();
                list.add(viewLike(shareBoardSeq, memberSeq));
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

        for (ShareBoardScrapMapping shareBoardScrapMapping : boardList) {
            try {
                Long shareBoardSeq = shareBoardScrapMapping.getSeq();
                list.add(viewScrap(shareBoardSeq, memberSeq));
            } catch (Exception e) {
                throw new WTException(e.getMessage()); // Todo : 바꾸기
            }

        }

        return list;
    }

    @Override
    @Transactional
    public ResponseShareBoardContentDto viewContent(Long shareBoardSeq) throws WTException {
        if (!shareBoardRepository.existsById(shareBoardSeq) || !shareBoardRepository.existsBySeqAndIsDeletedFalse(shareBoardSeq)) {
            throw new WTException("존재하지 않는 게시글입니다.");
        }

        ResponseShareBoardContentDto responseShareBoardContentDto = new ResponseShareBoardContentDto();

        responseShareBoardContentDto.setShareBoardSeq(shareBoardSeq);

        int isHitUpdated = shareBoardRepository.updateHit(shareBoardSeq);
        if (isHitUpdated != 1) {
            throw new WTException("글 상세 조회 오류");
        }

        ShareBoard shareBoard = shareBoardRepository.findBySeqAndIsDeletedFalse(shareBoardSeq);

        try {
            responseShareBoardContentDto.setTitle(shareBoard.getTitle());
            responseShareBoardContentDto.setContent(shareBoard.getContent());

            responseShareBoardContentDto.setMember(getMemberNicknameUrl(shareBoard.getMemberSeq()));

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
    public ResponseRecordDto viewRecord(Long shareBoardSeq) throws WTException {
        if (!shareBoardRepository.existsById(shareBoardSeq) || !shareBoardRepository.existsBySeqAndIsDeletedFalse(shareBoardSeq)) {
            throw new WTException("존재하지 않는 게시글입니다.");
        }

        ResponseRecordDto responseRecordDto = new ResponseRecordDto();

        try {
            responseRecordDto.setShareBoardSeq(shareBoardSeq);

            Long recordSeq = shareBoardRepository.findRecordSeqBySeqAndIsDeletedFalse(shareBoardSeq).getRecordSeq();

            responseRecordDto.setRecordSeq(recordSeq);

            ResponseViewDto responseViewDto = recordService.view(recordSeq);
            responseRecordDto.setAddress(responseViewDto.getAddress());
            responseRecordDto.setPoints(responseViewDto.getPoints());
            responseRecordDto.setDuration(responseViewDto.getDuration());
            responseRecordDto.setDistance(responseViewDto.getDistance());

            return responseRecordDto;
        } catch (Exception e) {
            throw new WTException(e.getMessage()); // Todo : 고치기
        }

    }

    @Override
    public List<ResponseCommentDto> viewComment(Long shareBoardSeq) throws WTException {
        if (!shareBoardRepository.existsById(shareBoardSeq) || !shareBoardRepository.existsBySeqAndIsDeletedFalse(shareBoardSeq)) {
            throw new WTException("존재하지 않는 게시글입니다.");
        }

        List<ShareBoardComment> commentDomainList = shareBoardCommentRepository.findAllByShareBoardSeqAndIsDeletedFalse(shareBoardSeq);
        List<ResponseCommentDto> list = new ArrayList<>();

        for (ShareBoardComment shareBoardComment : commentDomainList) {
            try {
                ResponseCommentDto responseCommentDto = new ResponseCommentDto();

                responseCommentDto.setCommentSeq(shareBoardComment.getSeq());
                responseCommentDto.setShareBoardSeq(shareBoardSeq);
                responseCommentDto.setContent(shareBoardComment.getContent());
                responseCommentDto.setMember(getMemberNicknameUrl(shareBoardComment.getMemberSeq()));
                responseCommentDto.setCreated_at(shareBoardComment.getCreatedBy());

                list.add(responseCommentDto);
            } catch (Exception e) {
                throw new WTException(e.getMessage()); // Todo : 고치기
            }
        }

        return list;
    }

    @Override
    public ResponseLikeDto viewLike(Long shareBoardSeq, Long memberSeq) throws WTException {
        if (!shareBoardRepository.existsById(shareBoardSeq) || !shareBoardRepository.existsBySeqAndIsDeletedFalse(shareBoardSeq)) {
            throw new WTException("존재하지 않는 게시글입니다.");
        }

        ResponseLikeDto responseLikeDto = new ResponseLikeDto();

        try {
            responseLikeDto.setShareBoardSeq(shareBoardSeq);
            responseLikeDto.setLikeCount(shareBoardLikeRepository.countAllByShareBoardSeq(shareBoardSeq));
            responseLikeDto.setLiked(shareBoardLikeRepository.existsByShareBoardSeqAndMemberSeq(shareBoardSeq, memberSeq));

            return responseLikeDto;
        } catch (Exception e) {
            throw new WTException(e.getMessage()); // Todo : 고치기
        }
    }

    @Override
    public ResponseScrapDto viewScrap(Long shareBoardSeq, Long memberSeq) throws WTException {
        if (!shareBoardRepository.existsById(shareBoardSeq) || !shareBoardRepository.existsBySeqAndIsDeletedFalse(shareBoardSeq)) {
            throw new WTException("존재하지 않는 게시글입니다.");
        }

        ResponseScrapDto responseScrapDto = new ResponseScrapDto();

        try {
            responseScrapDto.setShareBoardSeq(shareBoardSeq);

            Long recordSeq = shareBoardRepository.findRecordSeqBySeqAndIsDeletedFalse(shareBoardSeq).getRecordSeq();

            responseScrapDto.setRecordSeq(recordSeq);
            responseScrapDto.setScrapCount(scrapRepository.countAllByRecordSeq(recordSeq));
            responseScrapDto.setScraped(scrapRepository.existsByRecordSeqAndMemberSeq(recordSeq, memberSeq));

            return responseScrapDto;
        } catch (Exception e) {
            throw new WTException(e.getMessage()); // Todo : 고치기
        }
    }

    @Override
    public void modify(Long memberSeq, Long shareBoardSeq, RequestShareBoardModifyDto requestShareBoardModifyDto) throws WTException {
        if (!shareBoardRepository.existsById(shareBoardSeq) || !shareBoardRepository.existsBySeqAndIsDeletedFalse(shareBoardSeq)) {
            throw new WTException("존재하지 않는 게시글입니다.");
        }


        try {
            ShareBoard shareBoard = shareBoardRepository.findBySeqAndIsDeletedFalse(shareBoardSeq);

            if (!Objects.equals(memberSeq, shareBoard.getMemberSeq())) {
                throw new WTException("게시글 수정 실패"); // Todo : 고치기
            }
            shareBoard.update(requestShareBoardModifyDto.getTitle(), requestShareBoardModifyDto.getContent());
            shareBoardRepository.save(shareBoard);
        } catch (Exception e) {
            throw new WTException("수정 실패"); // Todo : 고치기
        }

    }

    @Override
    public void delete(Long memberSeq, Long shareBoardSeq) throws WTException {
        if (!shareBoardRepository.existsById(shareBoardSeq) || !shareBoardRepository.existsBySeqAndIsDeletedFalse(shareBoardSeq)) {
            throw new WTException("존재하지 않는 게시글입니다.");
        }

        ShareBoard shareBoard = shareBoardRepository.findBySeqAndIsDeletedFalse(shareBoardSeq);

        if (!Objects.equals(memberSeq, shareBoard.getMemberSeq())) {
            throw new WTException("게시글 삭제 실패"); // Todo : 고치기
        }

        try {
            shareBoard.delete(shareBoard);
            shareBoardRepository.save(shareBoard);
        } catch (Exception e) {
            throw new WTException(e.getMessage()); // Todo : 고치기
        }

    }

    @Override
    public void like(Long shareBoardSeq, Long memberSeq) throws WTException {
        if (!shareBoardRepository.existsById(shareBoardSeq) || !shareBoardRepository.existsBySeqAndIsDeletedFalse(shareBoardSeq)) {
            throw new WTException("존재하지 않는 게시글입니다.");
        }

        ShareBoardMemberMapping shareBoardMemberMapping = shareBoardRepository.findMemberSeqBySeqAndIsDeletedFalse(shareBoardSeq);

        if (shareBoardMemberMapping == null) {
            throw new WTException("좋아요 오류");
        }

        if (Objects.equals(shareBoardMemberMapping.getMemberSeq(), memberSeq)) {
            throw new WTException("자신의 글에 좋아요를 누를 수 없습니다.");
        }

        if (shareBoardLikeRepository.existsByShareBoardSeqAndMemberSeq(shareBoardSeq, memberSeq)) {
            throw new WTException("이미 좋아요를 누른 글입니다.");
        }

        try {
            ShareBoardLike shareBoardLike = ShareBoardLike.builder()
                    .shareBoardSeq(shareBoardSeq)
                    .memberSeq(memberSeq)
                    .build();
            shareBoardLikeRepository.save(shareBoardLike);
        } catch (Exception e) {
            throw new WTException("좋아요에 실패하였습니다.");
        }
    }

    @Override
    public void likeCancel(Long shareBoardSeq, Long memberSeq) throws WTException {
        if (!shareBoardRepository.existsById(shareBoardSeq) || !shareBoardRepository.existsBySeqAndIsDeletedFalse(shareBoardSeq)) {
            throw new WTException("존재하지 않는 게시글입니다.");
        }

        if (!shareBoardLikeRepository.existsByShareBoardSeqAndMemberSeq(shareBoardSeq, memberSeq)) {
            throw new WTException("좋아요 취소 실패");
        }

        try {
            shareBoardLikeRepository.deleteByShareBoardSeqAndMemberSeq(shareBoardSeq, memberSeq);
        } catch (Exception e) {
            throw new WTException("좋아요 취소에 실패하였습니다.");
        }
    }

    public ResponseMemberDto getMemberNicknameUrl(Long memberSeq) {
        ResponseMemberDto responseMemberDto = new ResponseMemberDto();

        NicknameUrlMapping m = memberRepository.findNickNameAndUrlBySeq(memberSeq);
        responseMemberDto.setNickname(m.getNickname());
        responseMemberDto.setProfilePic(m.getUrl());

        return responseMemberDto;
    }
}
