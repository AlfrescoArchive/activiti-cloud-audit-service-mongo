package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.runtime.api.model.impl.TaskImpl;

public class TaskMongoDeserializer extends MongoDeserializer<TaskImpl>{

    private static final long serialVersionUID = -9079578363905288315L;

    public TaskMongoDeserializer() {
        super(TaskImpl.class);
    }
}
