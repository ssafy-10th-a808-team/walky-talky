package com.ssafy.backend.record.controller;

import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;
import com.ssafy.backend.record.service.RecordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

            if(!recordService.registRecord(memberSeq, requestRegistRecordDto)){
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

}
