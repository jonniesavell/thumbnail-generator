package com.indigententerprises.thumbnailgenerator.services;

public interface ValidationService<T> {
    void validate(final T t) throws ValidationException;
}
