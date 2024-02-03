package com.ssafy.backend.scrapRecord.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.record.dto.mapping.ListMapping;
import com.ssafy.backend.record.dto.response.ResponseViewDto;

import java.util.List;

public interface ScrapRecordService {
    void scrap(Long recordSeq, Long memberSeq) throws WTException;

    void cancel(Long recordSeq, Long memberSeq) throws WTException;

    ResponseViewDto view(Long recordSeq, Long memberSeq) throws WTException;

    List<ListMapping> list(Long memberSeq) throws WTException;

    int getScrapcount(Long recordSeq) throws WTException;

    boolean getIsScraped(Long recordSeq, Long memberSeq) throws WTException;
}
