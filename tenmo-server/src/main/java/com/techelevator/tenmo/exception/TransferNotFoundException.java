package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "TEnmo transfer not found.")
public class TransferNotFoundException extends Exception {

    public TransferNotFoundException() {
        super("TEnmo transfer not found.");
    }
}
