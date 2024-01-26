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
            String memberId = (String) request.getAttribute("memberId");

            Long seq = recordService.startRecord(memberId);

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
            String memberId = (String) request.getAttribute("memberId");

            // 이거도 필터에서 먼저 사용자 정보 꺼내서 넘겨준 다음에 할 수 있음
            // 그럼 그 사용자 정보와 입력받은 정보들을 통해서 DB에 저장

            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }


    }

}
