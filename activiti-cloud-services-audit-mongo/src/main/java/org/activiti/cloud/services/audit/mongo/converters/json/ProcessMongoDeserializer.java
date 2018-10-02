package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.runtime.api.model.impl.ProcessInstanceImpl;

public class ProcessMongoDeserializer extends MongoDeserializer<ProcessInstanceImpl> {

    public ProcessMongoDeserializer() {
        super(ProcessInstanceImpl.class);
    }
}
