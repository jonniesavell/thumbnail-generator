package com.indigententerprises.thumbnailgenerator;

import com.indigententerprises.thumbnailgenerator.components.ThumbnailGeneratorService;
import com.indigententerprises.thumbnailgenerator.services.ValidationException;

import com.indigententerprises.services.common.SystemException;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Lambda function entry point. You can change to use other pojo type or implement
 * a different RequestHandler.
 *
 * @see <a href=https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html>Lambda Java Handler</a> for more information
 */
public class App implements RequestHandler<S3Event, Void> {

    private final ThumbnailGeneratorService thumbnailGeneratorService;
    private final ArrayList<Integer> widths;

    public App() throws SystemException {
        thumbnailGeneratorService = DependencyFactory.newThumbnailGenerator();

        widths = new ArrayList<>();
        widths.add(100);
        widths.add(200);
    }

    @Override
    public Void handleRequest(final S3Event event, final Context context) {

        final List<S3EventNotification.S3EventNotificationRecord> records = event.getRecords();

        for (final S3EventNotification.S3EventNotificationRecord record : records) {
            // final String srcBucket = record.getS3().getBucket().getName();
            // we are statically wired to receive from this bucket
            final String key = record.getS3().getObject().getUrlDecodedKey();

            try {
                thumbnailGeneratorService.generateThumbnails(key, widths);
            } catch (ValidationException | NoSuchElementException e) {
                // swallow:
                //   (1) validation-exception means that key refers to an unsupported image type
                //   (2) no-such-element-exception means that either an object referred to by key does not exist
                //         or the bucket itself does not exist (this latter possibility identifies a configuration error)
            } catch (IOException | SystemException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
