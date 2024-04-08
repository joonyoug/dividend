package com.example.sample.exception.impl;

import com.example.sample.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class FailedScrapTicker extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "ticker를 불러오기 실패했습니다.";
    }
}
