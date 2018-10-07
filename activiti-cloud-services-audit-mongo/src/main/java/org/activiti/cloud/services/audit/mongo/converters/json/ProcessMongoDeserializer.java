package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.runtime.api.model.impl.ProcessInstanceImpl;

public class ProcessMongoDeserializer extends MongoDeserializer<ProcessInstanceImpl> {

    private static final long serialVersionUID = -713869924647348780L;

    public ProcessMongoDeserializer() {
        super(ProcessInstanceImpl.class);
    }
}
