
package com.indigententerprises.thumbnailgenerator;

import com.indigententerprises.components.ObjectStorageComponent;
import com.indigententerprises.factories.ObjectStoreFactory;
import com.indigententerprises.services.common.SystemException;
import com.indigententerprises.services.objects.IObjectService;
import com.indigententerprises.thumbnail.components.ThumbnailService;

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
}
