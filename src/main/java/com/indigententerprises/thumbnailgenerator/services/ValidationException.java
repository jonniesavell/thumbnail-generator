package com.indigententerprises.thumbnailgenerator.services;

public class ValidationException extends RuntimeException {

    public ValidationException(final String msg) {
        super(msg);
    }

    public ValidationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
