package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (code = HttpStatus.NOT_FOUND, reason = "TEnmo user not found.")

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("TEnmo user not found.");
    }
}