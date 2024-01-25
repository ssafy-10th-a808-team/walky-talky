package com.ssafy.backend.record.controller;

import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;
import com.ssafy.backend.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/walk")
public class RecordController {

    private final RecordService recordService;

    @PostMapping("regist-record")
    public ResponseEntity<?> registRecord(@RequestHeader HttpHeaders requestHeader, @RequestBody RequestRegistRecordDto requestRegistRecordDto){
        Map<String, String> resultMap = new HashMap<>();

        // 이거도 필터에서 먼저 사용자 정보 꺼내서 넘겨준 다음에 할 수 있음
        // 그럼 그 사용자 정보와 입력받은 정보들을 통해서 DB에 저장

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}
