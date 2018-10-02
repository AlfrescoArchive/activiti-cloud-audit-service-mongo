package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.api.process.model.SequenceFlow;

public class SequenceMongoSerializer extends MongoSerializer<SequenceFlow> {

    public SequenceMongoSerializer() {
        super(SequenceFlow.class);
    }
}
