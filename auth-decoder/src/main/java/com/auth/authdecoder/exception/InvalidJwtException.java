package com.auth.authdecoder.exception;

public class InvalidJwtException extends RuntimeException
{
    public InvalidJwtException(String message, Throwable e)
    {
        super(message, e);
    }


    public InvalidJwtException(String message)
    {
        super(message);
    }
}
