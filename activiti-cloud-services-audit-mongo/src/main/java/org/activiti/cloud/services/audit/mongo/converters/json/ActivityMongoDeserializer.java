package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.runtime.api.model.impl.BPMNActivityImpl;

public class ActivityMongoDeserializer extends MongoDeserializer<BPMNActivityImpl>{

    public ActivityMongoDeserializer() {
        super(BPMNActivityImpl.class);
    }
}
