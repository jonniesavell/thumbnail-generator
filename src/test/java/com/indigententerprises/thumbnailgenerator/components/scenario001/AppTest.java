package com.indigententerprises.thumbnailgenerator.components.scenario001;

import com.indigententerprises.thumbnailgenerator.components.ThumbnailGeneratorService;
import com.indigententerprises.thumbnailgenerator.services.ValidationException;

import com.indigententerprises.services.common.SystemException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class AppTest {

    /**
     * the filename "pants.txt" is invalid from the perspective of thumbnail generation because
     *   the txt extension denotes something that is not binary. the purpose of this test is to
     *   document that ValidationException is the correct response to an input of an invalid
     *   type.
     */
    @Test
    public void test() throws SystemException {

        final ThumbnailGeneratorService systemUnderTest = DependencyFactory.newThumbnailGenerator();

        assertThrows(ValidationException.class, () ->
                systemUnderTest.generateThumbnails("pants.txt", Arrays.asList(100, 200)),
                "expected ValidationException which failed to materialize"
        );
    }
}
