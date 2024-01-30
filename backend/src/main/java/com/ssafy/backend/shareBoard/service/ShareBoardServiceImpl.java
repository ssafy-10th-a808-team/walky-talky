package com.ssafy.backend.shareBoard.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.record.domain.Record;
import com.ssafy.backend.record.dto.PointMapping;
import com.ssafy.backend.record.repository.RecordDetailRepository;
import com.ssafy.backend.record.repository.RecordRepository;
import com.ssafy.backend.region.service.RegionService;
import com.ssafy.backend.shareBoard.domain.ShareBoard;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;
import com.ssafy.backend.shareBoard.dto.response.ResponseShareBoardListDto;
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

    public List<ResponseShareBoardListDto> list() throws WTException {
        List<ShareBoard> boardList = shareBoardRepository.findAll();
        List<ResponseShareBoardListDto> list = new ArrayList<>();

        ResponseShareBoardListDto responseShareBoardListDto = new ResponseShareBoardListDto();
        for (ShareBoard article : boardList) {
            try {
                Optional<Member> m = memberRepository.findById(article.getMemberSeq());
                Optional<Record> r = recordRepository.findById(article.getRecordSeq());

                if (m.isEmpty() || r.isEmpty()) {
                    throw new WTException("글 목록 불러오기에 실패했습니다.");
                }

                Member member = m.get();
                Record record = r.get();

                responseShareBoardListDto.setNickname(member.getNickname());
                responseShareBoardListDto.setUrl(member.getUrl());

                responseShareBoardListDto.setTitle(article.getTitle());

                responseShareBoardListDto.setAddress(regionService.findAddress(record.getRegionCd()));

                List<PointMapping> points = recordDetailRepository.findLatitudeAndLongitudeByRecordSeq(article.getRecordSeq());
                responseShareBoardListDto.setPoints(points);

                responseShareBoardListDto.setDuration(record.getDuration());
                responseShareBoardListDto.setDistance(record.getDistance());

                responseShareBoardListDto.setCreate_at(article.getCreatedAt());
                responseShareBoardListDto.setLikeCount(article.getLikeCount());
                responseShareBoardListDto.setCommentCount(article.getCommentCount());
                responseShareBoardListDto.setScrapCount(record.getScrapedCount());
                responseShareBoardListDto.setHit(article.getHit());

                list.add(responseShareBoardListDto);
            } catch (Exception e) {
                throw new WTException(e.getMessage()); // Todo : 개발 끝나고 고치기
            }

        }

        return list;
    }

}
