package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.api.process.model.BPMNActivity;

public class ActivityMongoSerializer extends MongoSerializer<BPMNActivity> {

    private static final long serialVersionUID = 4574931419741161780L;

    public ActivityMongoSerializer() {
        super(BPMNActivity.class);
    }
}
