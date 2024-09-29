package com.indigententerprises.thumbnailgenerator;

import com.indigententerprises.thumbnailgenerator.components.FilenameExtensionValidator;
import com.indigententerprises.thumbnailgenerator.components.FilenameGenerator;
import com.indigententerprises.thumbnailgenerator.components.ImageTestAgent;
import com.indigententerprises.thumbnailgenerator.components.ThumbnailGeneratorService;
import com.indigententerprises.thumbnailgenerator.services.IObjectServicePair;

import com.indigententerprises.components.ObjectStorageComponent;
import com.indigententerprises.factories.ObjectStoreFactory;
import com.indigententerprises.services.common.SystemException;
import com.indigententerprises.services.objects.IObjectService;

import com.indigententerprises.thumbnail.components.ThumbnailService;

import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * The module containing all dependencies required by the {@link App}.
 */
public class DependencyFactory {

    private static final String SOURCE_BUCKET = "SOURCE_BUCKET";
    private static final String TARGET_BUCKET = "TARGET_BUCKET";

    private DependencyFactory() {}

    public static IObjectServicePair newIObjectServicePair() throws SystemException {

        final String sourceBucket = System.getenv(SOURCE_BUCKET);
        final ObjectStorageComponent sourceObjectStorageComponent =
                ObjectStoreFactory.createObjectStorageComponent(sourceBucket);
        final IObjectService source = sourceObjectStorageComponent.getObjectService();

        final String targetBucket = System.getenv(TARGET_BUCKET);
        final ObjectStorageComponent targetObjectStorageComponent =
                ObjectStoreFactory.createObjectStorageComponent(targetBucket);
        final IObjectService target = targetObjectStorageComponent.getObjectService();

        final IObjectServicePair result = new IObjectServicePair(source, target);
        return result;
    }

    public static com.indigententerprises.thumbnail.services.ThumbnailService newThumbnailService() {

        final ThumbnailService result = new ThumbnailService(0.90);
        return result;
    }

    public static Predicate<String> newPredicate() {

        final Predicate<String> result = new ImageTestAgent();
        return result;
    }

    public static com.indigententerprises.thumbnailgenerator.services.ValidationService<String> newValidationService() {

        final com.indigententerprises.thumbnailgenerator.services.ValidationService<String> result =
                new FilenameExtensionValidator(newPredicate());
        return result;
    }

    public static BiFunction<String, String, String> newFilenameGeneratorService() {
        final FilenameGenerator result = new FilenameGenerator();
        return result;
    }

    public static ThumbnailGeneratorService newThumbnailGenerator() throws SystemException {

        final ThumbnailGeneratorService result = new ThumbnailGeneratorService(
                newIObjectServicePair(),
                newThumbnailService(),
                newValidationService(),
                newFilenameGeneratorService(),
                "JPEG"
        );
        return result;
    }
}
