package com.indigententerprises.thumbnailgenerator.components.scenario003;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.indigententerprises.thumbnailgenerator.components.FilenameGenerator;
import org.junit.jupiter.api.Test;

public class ServiceTest {

    @Test
    public void test() {

        final FilenameGenerator systemUnderTest = new FilenameGenerator();
        final String input1 = "abcdefghijklmnopqrstuvwxyz.jpg";
        final String input2 = "Version-001";
        final String expectedResult = "abcdefghijklmnopqrstuvwxyzVersion-001";
        final String actualResult = systemUnderTest.apply(input1, input2);

        assertEquals(expectedResult, actualResult);
    }
}
