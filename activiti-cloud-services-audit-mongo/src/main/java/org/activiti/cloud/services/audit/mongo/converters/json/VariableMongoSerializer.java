package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.api.model.shared.model.VariableInstance;

public class VariableMongoSerializer extends MongoSerializer<VariableInstance> {

    private static final long serialVersionUID = -6851599066260243788L;

    public VariableMongoSerializer() {
        super(VariableInstance.class);
    }
}
