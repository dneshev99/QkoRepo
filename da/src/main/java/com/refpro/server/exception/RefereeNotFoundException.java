package com.refpro.server.exception;

public class RefereeNotFoundException extends AbstractRestException{
    public RefereeNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
