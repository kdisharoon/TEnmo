package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (code = HttpStatus.NOT_FOUND, reason = "TEnmo account not found.")

public class AcctNotFoundException extends Exception {

    public AcctNotFoundException() {
        super("TEnmo account not found.");
    }
}
