package com.techelevator.tenmo.exception;


public class AcctNotFoundException extends Exception {

    public AcctNotFoundException() {
        super();
    }

    public AcctNotFoundException(String message) {
        super("TEnmo account not found.");
    }

}
