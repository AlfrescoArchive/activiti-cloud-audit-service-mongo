package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.runtime.api.model.impl.BPMNActivityImpl;

public class ActivityMongoDeserializer extends MongoDeserializer<BPMNActivityImpl>{

    private static final long serialVersionUID = 2165183082298011692L;

    public ActivityMongoDeserializer() {
        super(BPMNActivityImpl.class);
    }
}
