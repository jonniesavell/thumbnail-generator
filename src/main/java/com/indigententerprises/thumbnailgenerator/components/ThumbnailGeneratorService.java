package com.indigententerprises.thumbnailgenerator.components;

import com.indigententerprises.thumbnailgenerator.services.IObjectServicePair;
import com.indigententerprises.thumbnailgenerator.services.ValidationException;
import com.indigententerprises.thumbnailgenerator.services.ValidationService;

import com.indigententerprises.domain.objects.Handle;
import com.indigententerprises.services.common.SystemException;
import com.indigententerprises.thumbnail.domain.ImageData;
import com.indigententerprises.thumbnail.services.ThumbnailService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public class ThumbnailGeneratorService {

    private final IObjectServicePair objectServicePair;
    private final ThumbnailService thumbnailService;
    private final ValidationService<String> validationService;
    private final BiFunction<String, String, String> filenameGeneratorService;
    private final String type;

    public ThumbnailGeneratorService(
            final IObjectServicePair objectServicePair,
            final ThumbnailService thumbnailService,
            final ValidationService<String> validationService,
            final BiFunction<String, String, String> filenameGeneratorService,
            final String type
    ) {
        this.objectServicePair = objectServicePair;
        this.thumbnailService = thumbnailService;
        this.validationService = validationService;
        this.filenameGeneratorService = filenameGeneratorService;
        this.type = type;
    }

    public void generateThumbnails(
            final String key,
            final Collection<Integer> widths
    ) throws ValidationException, NoSuchElementException, IOException, SystemException {

        validationService.validate(key);

        final Handle handle = new Handle(key);
        final File parentDirectory = new File("/tmp");
        final File tempFile = File.createTempFile("image", null, parentDirectory);

        // retrieve the source object
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
            objectServicePair.source.retrieveObject(handle, fileOutputStream);
        }

        for (final int width : widths) {
            try (FileInputStream fileInputStream = new FileInputStream(tempFile)) {
                try {
                    final ImageData imageData =
                            thumbnailService.resizeImage(
                                    fileInputStream,
                                    width,
                                    type
                            );
                    final BufferedImage thumbnailImage = imageData.bufferedImage;
                    final String widthByHeight =
                            "_" + width + "X" + thumbnailImage.getHeight();
                    final String newKey = filenameGeneratorService.apply(key, widthByHeight) + ".jpg";
                    final Handle newHandle = new Handle(newKey);
                    final File thumbnailFile = File.createTempFile("thumb", null, parentDirectory);

                    try {
                        ImageIO.write(thumbnailImage, "jpeg", thumbnailFile);

                        try (FileInputStream thumbFileInputStream = new FileInputStream(thumbnailFile)) {
                            objectServicePair.target.storeObjectAndMetaData(
                                    thumbFileInputStream,
                                    newHandle,
                                    (int) thumbnailFile.length(),
                                    Collections.emptyMap()
                            );
                        }
                    } finally {
                        if (!thumbnailFile.delete()) {
                            thumbnailFile.deleteOnExit();
                        }
                    }
                } catch (RuntimeException e) {
                    // swallow: cannot fail the batch because of a problem with a part
                }
            }
        }
    }
}
