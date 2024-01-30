package com.ssafy.backend.shareBoard.controller;

import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;
import com.ssafy.backend.shareBoard.dto.response.ResponseShareBoardListDto;
import com.ssafy.backend.shareBoard.service.ShareBoardService;
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
@RequestMapping("/api/share-board")
public class ShareBoardController {

    private final ShareBoardService shareBoardService;

    @PostMapping("/write")
    public ResponseEntity<?> write(HttpServletRequest request, @RequestBody RequestShareBoardWriteDto requestShareBoardWriteDto) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            try {
                shareBoardService.write(memberSeq, requestShareBoardWriteDto);
            } catch (Exception e) {
                resultMap.put("msg", e.getMessage());
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
    public ResponseEntity<?> list() {
        Map<String, Object> resultMap = new HashMap<>();

        List<ResponseShareBoardListDto> list;

        try {
            list = shareBoardService.list();
        } catch (Exception e) {
            resultMap.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", list);
        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}
