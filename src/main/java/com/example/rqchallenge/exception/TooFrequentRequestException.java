package com.example.rqchallenge.exception;

import lombok.Getter;

@Getter
public class TooFrequentRequestException extends RuntimeException {
    private final String message;

    public TooFrequentRequestException(String msg) {
        super();
        this.message = msg;
    }
}
