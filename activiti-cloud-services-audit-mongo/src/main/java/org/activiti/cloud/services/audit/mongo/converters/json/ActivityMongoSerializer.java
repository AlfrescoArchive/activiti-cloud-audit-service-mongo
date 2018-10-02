package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.api.process.model.BPMNActivity;

public class ActivityMongoSerializer extends MongoSerializer<BPMNActivity> {

    public ActivityMongoSerializer() {
        super(BPMNActivity.class);
    }
}
