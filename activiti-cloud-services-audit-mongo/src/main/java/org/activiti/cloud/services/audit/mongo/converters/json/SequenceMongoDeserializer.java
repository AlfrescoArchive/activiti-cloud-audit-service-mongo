package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.runtime.api.model.impl.SequenceFlowImpl;

public class SequenceMongoDeserializer extends MongoDeserializer<SequenceFlowImpl> {

    public SequenceMongoDeserializer() {
        super(SequenceFlowImpl.class);
    }

}
