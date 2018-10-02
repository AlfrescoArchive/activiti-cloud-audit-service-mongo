package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.api.model.shared.model.VariableInstance;

public class VariableMongoSerializer extends MongoSerializer<VariableInstance> {

    public VariableMongoSerializer() {
        super(VariableInstance.class);
    }
}
