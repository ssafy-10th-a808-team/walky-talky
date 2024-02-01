package com.ssafy.backend.scrapRecord.controller;

import com.ssafy.backend.record.dto.mapping.ListMapping;
import com.ssafy.backend.record.dto.response.ResponseViewDto;
import com.ssafy.backend.scrapRecord.service.ScrapRecordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scrap-record")
public class ScrapRecordController {

    private final ScrapRecordService scrapRecordService;

    @PostMapping("{recordSeq}/scrap")
    public ResponseEntity<?> scrap(HttpServletRequest request, @PathVariable Long recordSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            scrapRecordService.scrap(recordSeq, memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("{recordSeq}/cancel")
    public ResponseEntity<?> cancel(HttpServletRequest request, @PathVariable Long recordSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        try {
            scrapRecordService.cancel(recordSeq, memberSeq);
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

        List<ListMapping> scrapList;
        try {
            scrapList = scrapRecordService.list(memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", scrapList);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/view/{recordSeq}")
    public ResponseEntity<?> view(HttpServletRequest request, @PathVariable Long recordSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");

        ResponseViewDto responseViewDto;
        try {
            responseViewDto = scrapRecordService.view(recordSeq, memberSeq);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", responseViewDto);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}
