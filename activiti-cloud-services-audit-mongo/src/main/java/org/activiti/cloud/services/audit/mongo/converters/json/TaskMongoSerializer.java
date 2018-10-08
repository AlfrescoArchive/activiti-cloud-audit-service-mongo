package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.api.task.model.Task;

public class TaskMongoSerializer extends MongoSerializer<Task>{

    public TaskMongoSerializer() {
        super(Task.class);
    }

    private static final long serialVersionUID = -8863110679697868451L;

}
