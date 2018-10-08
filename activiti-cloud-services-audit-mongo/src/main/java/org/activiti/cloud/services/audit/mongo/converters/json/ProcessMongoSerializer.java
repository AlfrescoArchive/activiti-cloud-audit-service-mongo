package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.api.process.model.ProcessInstance;

public class ProcessMongoSerializer extends MongoSerializer<ProcessInstance> {

    private static final long serialVersionUID = -876363298177822188L;

    public ProcessMongoSerializer() {
        super(ProcessInstance.class);
    }
}
