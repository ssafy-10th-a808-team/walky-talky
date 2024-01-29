package com.ssafy.backend.record.controller;

import com.ssafy.backend.record.dto.request.*;
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

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            Long seq = recordService.startRecord(memberSeq);

            Map<String, Long> returnSeq = new HashMap<>();
            returnSeq.put("seq", seq);

            resultMap.put("data", returnSeq);
            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @PostMapping("/regist-record")
    public ResponseEntity<?> registRecord(HttpServletRequest request, @RequestBody RequestRegistRecordDto requestRegistRecordDto) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            if (!recordService.registRecord(memberSeq, requestRegistRecordDto)) {
                resultMap.put("message", "산책 기록 등록에 실패하였습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @PostMapping("/regist-comment")
    public ResponseEntity<?> registComment(HttpServletRequest request, @RequestBody RequestRegistCommentDto requestRegistCommentDto) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            if (!recordService.registComment(memberSeq, requestRegistCommentDto)) {
                resultMap.put("message", "산책 중 한줄평 등록에 실패하였습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @PostMapping("/regist-image")
    public ResponseEntity<?> registImage(HttpServletRequest request, @RequestPart("image") MultipartFile multipartFile, @RequestPart("json") RequestRegistImageDto requestRegistImageDto) {
        Map<String, Object> resultMap = new HashMap<>();

        requestRegistImageDto.setMultipartFile(multipartFile);

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            Long seq = recordService.registImage(memberSeq, requestRegistImageDto);
            if (seq == -1) {
                resultMap.put("message", "산책 중 사진 등록에 실패하였습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

            resultMap.put("message", "OK");

            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("seq", seq);
            resultMap.put("data", returnMap);

            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @PostMapping("/modify-image")
    public ResponseEntity<?> modifyImage(HttpServletRequest request, @RequestPart("image") MultipartFile multipartFile, @RequestPart("json") RequestModifyImageDto requestModifyImageDto) {
        Map<String, Object> resultMap = new HashMap<>();

        requestModifyImageDto.setMultipartFile(multipartFile);

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            if (!recordService.modifyImage(memberSeq, requestModifyImageDto)) {
                resultMap.put("message", "산책 중 사진 수정에 실패하였습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @PostMapping("/delete-image")
    public ResponseEntity<?> deleteImage(HttpServletRequest request, Long seq) {
        Map<String, Object> resultMap = new HashMap<>();


        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            if (!recordService.deleteImage(memberSeq, seq)) {
                resultMap.put("message", "산책 중 사진 삭제에 실패하였습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            List<ResponseListDto> list = recordService.list(memberSeq);

            Map<String, List<ResponseListDto>> returnMap = new HashMap<>();
            returnMap.put("list", list);

            resultMap.put("data", returnMap);
            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @GetMapping("/view/{recordSeq}")
    public ResponseEntity<?> view(HttpServletRequest request, @PathVariable("recordSeq") Long recordSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            ResponseViewDto responseViewDto = recordService.view(recordSeq);

            resultMap.put("data", responseViewDto);
            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @PostMapping("/modify/{recordSeq}")
    public ResponseEntity<?> modify(HttpServletRequest request, @PathVariable("recordSeq") Long recordSeq, @RequestBody RequestRecordModify requestRecordModify) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            if (recordService.modify(memberSeq, recordSeq, requestRecordModify)) {
                resultMap.put("message", "OK");
                return ResponseEntity.status(HttpStatus.OK).body(resultMap);
            } else {
                resultMap.put("message", "수정에 실패하였습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @PostMapping("/delete/{recordSeq}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable("recordSeq") Long recordSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            if (recordService.delete(memberSeq, recordSeq)) {
                resultMap.put("message", "OK");
                return ResponseEntity.status(HttpStatus.OK).body(resultMap);
            } else {
                resultMap.put("message", "삭제에 실패하였습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

}
