package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.runtime.api.model.impl.SequenceFlowImpl;

public class SequenceMongoDeserializer extends MongoDeserializer<SequenceFlowImpl> {

    private static final long serialVersionUID = -1558045682577013311L;

    public SequenceMongoDeserializer() {
        super(SequenceFlowImpl.class);
    }

}
