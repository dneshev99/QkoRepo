package com.refpro.server.exception;

public abstract class AbstractRestException extends Exception{
    public AbstractRestException(String errorMessage){
        super(errorMessage);
    }

    public AbstractRestException(){
    }
}
