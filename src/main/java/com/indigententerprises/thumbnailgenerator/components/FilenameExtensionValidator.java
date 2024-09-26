package com.indigententerprises.thumbnailgenerator.components;

import com.indigententerprises.thumbnailgenerator.services.ValidationException;
import com.indigententerprises.thumbnailgenerator.services.ValidationService;

import java.util.function.Predicate;

public class FilenameExtensionValidator implements ValidationService<String> {

    private final Predicate<String> predicate;

    public FilenameExtensionValidator(final Predicate<String> predicate) {
        this.predicate = predicate;
    }

    public void validate(final String filename) throws ValidationException {
        if (predicate.test(filename)) {
            // do nothing: this is success
        } else {
            throw new ValidationException(String.format("filename %s violates specification", filename));
        }
    }
}
