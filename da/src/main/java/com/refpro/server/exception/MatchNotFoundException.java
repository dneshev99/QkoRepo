package com.refpro.server.exception;


public class MatchNotFoundException extends AbstractRestException{
    public MatchNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
