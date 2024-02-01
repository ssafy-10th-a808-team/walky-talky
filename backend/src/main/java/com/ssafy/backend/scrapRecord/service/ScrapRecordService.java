package com.ssafy.backend.scrapRecord.service;

import com.ssafy.backend.global.error.WTException;

public interface ScrapRecordService {
    void scrap(Long recordSeq, Long memberSeq) throws WTException;
}
