package com.helloworld.callshop.rater.rate.factory;

public class InvalidParameterValueException extends RuntimeException{
    public InvalidParameterValueException() {
    }

    public InvalidParameterValueException(String message) {
        super(message);
    }

    public InvalidParameterValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParameterValueException(Throwable cause) {
        super(cause);
    }

    public InvalidParameterValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
