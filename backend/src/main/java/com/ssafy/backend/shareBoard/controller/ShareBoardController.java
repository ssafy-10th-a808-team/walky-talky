package com.ssafy.backend.shareBoard.controller;

import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardModifyDto;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;
import com.ssafy.backend.shareBoard.dto.response.*;
import com.ssafy.backend.shareBoard.service.ShareBoardService;
import com.ssafy.backend.shareBoardCommet.dto.response.ResponseShareBoardCommentDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/share-board")
public class ShareBoardController {

    private final ShareBoardService shareBoardService;

    @PostMapping("/write")
    public ResponseEntity<?> write(HttpServletRequest request, @RequestBody RequestShareBoardWriteDto requestShareBoardWriteDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            shareBoardService.write(memberSeq, requestShareBoardWriteDto);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/list/total-count")
    public ResponseEntity<?> getTotalCount(){
        Map<String, Object> resultMap = new HashMap<>();

        int totalCount = shareBoardService.getTotalCount();

        resultMap.put("data", totalCount);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/list/content")
    public ResponseEntity<?> listContent(HttpServletRequest request,
                                         @RequestParam(name="page", defaultValue = "0") int page) {
        Map<String, Object> resultMap = new HashMap<>();

        List<ResponseShareBoardDto> contentList;
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        try {
            contentList = shareBoardService.listContent(pageable);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", contentList);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/list/record")
    public ResponseEntity<?> listRecord(HttpServletRequest request, @RequestParam(name="page", defaultValue = "0") int page) {
        Map<String, Object> resultMap = new HashMap<>();

        List<ResponseRecordDto> recordList;
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        try {
            recordList = shareBoardService.listRecord(pageable);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", recordList);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/list/like")
    public ResponseEntity<?> listLike(HttpServletRequest request, @RequestParam(name="page", defaultValue = "0") int page) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        List<ResponseLikeDto> likeList;
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        try {
            likeList = shareBoardService.listLike(memberSeq, pageable);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", likeList);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/list/scrap")
    public ResponseEntity<?> listScrap(HttpServletRequest request, @RequestParam(name="page", defaultValue = "0") int page) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        List<ResponseScrapDto> scrapList;
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        try {
            scrapList = shareBoardService.listScrap(memberSeq, pageable);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", scrapList);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/view/{shareBoardSeq}/content")
    public ResponseEntity<?> viewContent(@PathVariable Long shareBoardSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        ResponseShareBoardContentDto responseShareBoardContentDto;
        try {
            responseShareBoardContentDto = shareBoardService.viewContent(shareBoardSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", responseShareBoardContentDto);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/view/{shareBoardSeq}/record")
    public ResponseEntity<?> viewRecord(@PathVariable Long shareBoardSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        ResponseRecordDto responseRecordDto;
        try {
            responseRecordDto = shareBoardService.viewRecord(shareBoardSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", responseRecordDto);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/view/{shareBoardSeq}/comment")
    public ResponseEntity<?> viewComment(@PathVariable Long shareBoardSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        List<ResponseShareBoardCommentDto> commentList;
        try {
            commentList = shareBoardService.viewComment(shareBoardSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", commentList);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/view/{shareBoardSeq}/like")
    public ResponseEntity<?> viewLike(HttpServletRequest request, @PathVariable Long shareBoardSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        ResponseLikeDto responseLikeDto;
        try {
            responseLikeDto = shareBoardService.viewLike(shareBoardSeq, memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", responseLikeDto);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/view/{shareBoardSeq}/scrap")
    public ResponseEntity<?> viewScrap(HttpServletRequest request, @PathVariable Long shareBoardSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        ResponseScrapDto responseScrapDto;
        try {
            responseScrapDto = shareBoardService.viewScrap(shareBoardSeq, memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", responseScrapDto);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/modify/{shareBoardSeq}")
    public ResponseEntity<?> modify(HttpServletRequest request, @PathVariable Long shareBoardSeq, @RequestBody RequestShareBoardModifyDto requestShareBoardModifyDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            shareBoardService.modify(memberSeq, shareBoardSeq, requestShareBoardModifyDto);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/delete/{shareBoardSeq}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Long shareBoardSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            shareBoardService.delete(memberSeq, shareBoardSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/{shareBoardSeq}/like")
    public ResponseEntity<?> like(HttpServletRequest request, @PathVariable Long shareBoardSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            shareBoardService.like(shareBoardSeq, memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/{shareBoardSeq}/like-cancel")
    public ResponseEntity<?> likeCancel(HttpServletRequest request, @PathVariable Long shareBoardSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            shareBoardService.likeCancel(shareBoardSeq, memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}
