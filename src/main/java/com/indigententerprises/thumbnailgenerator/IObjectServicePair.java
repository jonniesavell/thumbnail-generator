package com.indigententerprises.thumbnailgenerator;

import com.indigententerprises.services.objects.IObjectService;

public class IObjectServicePair {
    public final IObjectService source;
    public final IObjectService target;

    public IObjectServicePair(
            final IObjectService source,
            final IObjectService target
    ) {
        this.source = source;
        this.target = target;
    }
}
