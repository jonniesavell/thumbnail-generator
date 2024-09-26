package com.indigententerprises.thumbnailgenerator.components.scenario002;

import com.indigententerprises.thumbnailgenerator.components.ThumbnailGeneratorService;
import com.indigententerprises.thumbnailgenerator.services.ValidationException;

import com.indigententerprises.services.common.SystemException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class AppTest {

    /**
     * while "pants.jpg" is a legitimate name, the factory is wired with a ValidationService that is
     *   hardwired to reject the file name.
     */
    @Test
    public void test() throws SystemException {

        final ThumbnailGeneratorService systemUnderTest = DependencyFactory.newThumbnailGenerator();

        assertThrows(ValidationException.class, () ->
                systemUnderTest.generateThumbnails("pants.jpg", Arrays.asList(100, 200)),
                "expected ValidationException which failed to materialize"
        );
    }
}
