package com.refpro.server.exception;


public class InvalidInputException  extends AbstractRestException{
    public InvalidInputException(String errorMessage) {
        super(errorMessage);
    }
}
