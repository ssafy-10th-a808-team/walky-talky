package com.ssafy.backend.shareBoardCommet.controller;


import com.ssafy.backend.shareBoardCommet.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/share-board/{shareBoardSeq}/comment")
public class commentController {

    private final CommentService commentService;

    @PostMapping("write")
    public ResponseEntity<?> commentWrite(HttpServletRequest request,
                                          @RequestBody Map<String, String> map,
                                          @PathVariable(value = "shareBoardSeq") Long shareBoardSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            try {
                String content = map.get("content");
                commentService.commentWrite(shareBoardSeq, memberSeq, content);
            } catch (Exception e) {
                resultMap.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @PostMapping("/{commentSeq}/modify")
    public ResponseEntity<?> commentModify(HttpServletRequest request,
                                           @RequestBody Map<String, String> map,
                                           @PathVariable(value = "shareBoardSeq") Long shareBoardSeq,
                                           @PathVariable(value = "commentSeq") Long commentSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            try {
                String content = map.get("content");
                commentService.commentModify(shareBoardSeq, commentSeq, memberSeq, content);
            } catch (Exception e) {
                resultMap.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            resultMap.put("message", msg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @PostMapping("/{commentSeq}/delete")
    public ResponseEntity<?> commentDelete(HttpServletRequest request,
                                           @PathVariable(value = "shareBoardSeq") Long shareBoardSeq,
                                           @PathVariable(value = "commentSeq") Long commentSeq) {
        Map<String, Object> resultMap = new HashMap<>();

        String msg = (String) request.getAttribute("message");
        if (msg == null) {
            Long memberSeq = (Long) request.getAttribute("seq");

            try {
                commentService.commentDelete(shareBoardSeq, commentSeq, memberSeq);
            } catch (Exception e) {
                resultMap.put("message", e.getMessage());
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
