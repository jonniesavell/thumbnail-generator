package com.indigententerprises.thumbnailgenerator.components;

import org.apache.commons.io.FilenameUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

public class ImageTestAgent implements Predicate<String> {
    @Override
    public boolean test(final String imageFileName) {

        final String extension = FilenameUtils.getExtension(imageFileName);

        if (StringUtils.isBlank(extension)) {
            return false;
        } else {
            final String lowerCaseExtension = extension.toLowerCase();
            final boolean supported = switch (lowerCaseExtension) {
                case "jpeg", "jpg", "png" -> true;
                default                   -> false;
            };

            return supported;
        }
    }
}
