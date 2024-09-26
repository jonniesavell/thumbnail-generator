package com.indigententerprises.thumbnailgenerator.components;

import org.apache.commons.io.FilenameUtils;

import java.util.function.BiFunction;

public class FilenameGenerator implements BiFunction<String, String, String> {
    @Override
    public String apply(final String filename, final String toBeAppended) {

        final String basename = FilenameUtils.getBaseName(filename);
        final String newFilename = basename + toBeAppended;
        return newFilename;
    }
}
