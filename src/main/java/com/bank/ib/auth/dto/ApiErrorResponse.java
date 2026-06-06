package com.bank.ib.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class ApiErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;

}

