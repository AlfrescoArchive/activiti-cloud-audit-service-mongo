package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.api.process.model.SequenceFlow;

public class SequenceMongoSerializer extends MongoSerializer<SequenceFlow> {

    private static final long serialVersionUID = 7944110974691993478L;

    public SequenceMongoSerializer() {
        super(SequenceFlow.class);
    }
}
