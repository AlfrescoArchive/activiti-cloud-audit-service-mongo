package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.runtime.api.model.impl.TaskImpl;

public class TaskMongoDeserializer extends MongoDeserializer<TaskImpl>{

    public TaskMongoDeserializer() {
        super(TaskImpl.class);
    }
}
