package com.ssafy.backend.record.controller;

import com.ssafy.backend.record.dto.request.RequestRecordModify;
import com.ssafy.backend.record.dto.request.RequestRegistCommentDto;
import com.ssafy.backend.record.dto.request.RequestRegistImageDto;
import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;
import com.ssafy.backend.record.dto.response.ResponseListDto;
import com.ssafy.backend.record.dto.response.ResponseViewDto;
import com.ssafy.backend.record.service.RecordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/walk")
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/start-record")
    public ResponseEntity<?> startRecord(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        Long seq = recordService.startRecord(memberSeq);

        Map<String, Long> returnSeq = new HashMap<>();
        returnSeq.put("seq", seq);

        resultMap.put("data", returnSeq);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/regist-record")
    public ResponseEntity<?> registRecord(HttpServletRequest request, @RequestBody RequestRegistRecordDto requestRegistRecordDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            recordService.registRecord(memberSeq, requestRegistRecordDto);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/regist-comment")
    public ResponseEntity<?> registComment(HttpServletRequest request, @RequestBody RequestRegistCommentDto requestRegistCommentDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        Long seq;
        try {
            seq = recordService.registComment(memberSeq, requestRegistCommentDto);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("seq", seq);

        resultMap.put("message", "OK");
        resultMap.put("data", returnMap);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("modify-comment/{recordDetailSeq}")
    public ResponseEntity<?> registComment(HttpServletRequest request, @RequestBody Map<String, String> map, @PathVariable("recordDetailSeq") Long recordDetailSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        String comment = map.get("comment");

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            recordService.modifyComment(memberSeq, recordDetailSeq, comment);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("delete-comment/{recordDetailSeq}")
    public ResponseEntity<?> deleteComment(HttpServletRequest request, @PathVariable("recordDetailSeq") Long recordDetailSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            recordService.deleteComment(memberSeq, recordDetailSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/regist-image")
    public ResponseEntity<?> registImage(HttpServletRequest request, RequestRegistImageDto requestRegistImageDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        Long seq;
        try {
            seq = recordService.registImage(memberSeq, requestRegistImageDto);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("seq", seq);
        resultMap.put("data", returnMap);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/modify-image/{recordDetailSeq}")
    public ResponseEntity<?> modifyImage(HttpServletRequest request, MultipartFile multipartFile, @PathVariable("recordDetailSeq") Long recordDetailSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            recordService.modifyImage(memberSeq, recordDetailSeq, multipartFile);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/delete-image/{recordDetailSeq}")
    public ResponseEntity<?> deleteImage(HttpServletRequest request, @PathVariable("recordDetailSeq") Long recordDetailSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            recordService.deleteImage(memberSeq, recordDetailSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        List<ResponseListDto> list;
        try {
            list = recordService.list(memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", list);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/view/{recordSeq}")
    public ResponseEntity<?> view(HttpServletRequest request, @PathVariable("recordSeq") Long recordSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        ResponseViewDto responseViewDto;
        try {
            responseViewDto = recordService.view(memberSeq, recordSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", responseViewDto);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/modify/{recordSeq}")
    public ResponseEntity<?> modify(HttpServletRequest request, @PathVariable("recordSeq") Long recordSeq, @RequestBody RequestRecordModify requestRecordModify) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            recordService.modify(memberSeq, recordSeq, requestRecordModify);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/delete/{recordSeq}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable("recordSeq") Long recordSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            recordService.delete(memberSeq, recordSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/recommend-town")
    public ResponseEntity<?> recommendTown(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        List<ResponseListDto> list;
        try {
            list = recordService.recommendTown(memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", list);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/recommend-info")
    public ResponseEntity<?> recommendInfo(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        List<ResponseListDto> list;
        try {
            list = recordService.recommendInfo(memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", list);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/dislike/{recordSeq}")
    public ResponseEntity<?> dislike(HttpServletRequest request, @PathVariable("recordSeq") Long recordSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            recordService.dislike(recordSeq, memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}
