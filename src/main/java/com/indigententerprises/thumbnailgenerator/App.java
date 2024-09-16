package com.indigententerprises.thumbnailgenerator;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

import com.indigententerprises.domain.objects.Handle;
import com.indigententerprises.services.common.SystemException;
import com.indigententerprises.thumbnail.domain.ImageData;
import com.indigententerprises.thumbnail.services.ThumbnailService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Lambda function entry point. You can change to use other pojo type or implement
 * a different RequestHandler.
 *
 * @see <a href=https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html>Lambda Java Handler</a> for more information
 */
public class App implements RequestHandler<S3Event, Void> {

    private final IObjectServicePair objectServicePair;
    private final ThumbnailService thumbnailService;

    public App() throws SystemException {
        objectServicePair = DependencyFactory.newIObjectServicePair();
        thumbnailService = DependencyFactory.newThumbnailService();
    }

    @Override
    public Void handleRequest(final S3Event event, final Context context) {

        final List<S3EventNotification.S3EventNotificationRecord> records = event.getRecords();

        for (final S3EventNotification.S3EventNotificationRecord record : records) {
            // final String srcBucket = record.getS3().getBucket().getName();
            // we are statically wired to receive from this bucket
            final String key = record.getS3().getObject().getUrlDecodedKey();
            final Handle handle = new Handle(key);
            final int [] widths = { 100, 200 };
            final String type = "JPEG";

            try {
                final File parentDirectory = new File("/tmp");
                final File tempFile = File.createTempFile(null, null, parentDirectory);

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
                                    width + "X" + thumbnailImage.getHeight();
                            final String newKey = key + "_" + widthByHeight + ".jpg";
                            final Handle newHandle = new Handle(newKey);
                            objectServicePair.target.storeObjectAndMetaData(
                                    fileInputStream,
                                    newHandle,
                                    imageData.size,
                                    Collections.emptyMap()
                            );
                        } catch (RuntimeException e) {
                            // swallow: cannot fail the batch because of a problem with a part
                        }
                    }
                }
            } catch (NoSuchElementException e) {
                // swallow
            } catch (IOException | SystemException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
