package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.api.process.model.ProcessInstance;

public class ProcessMongoSerializer extends MongoSerializer<ProcessInstance> {

    public ProcessMongoSerializer() {
        super(ProcessInstance.class);
    }
}
