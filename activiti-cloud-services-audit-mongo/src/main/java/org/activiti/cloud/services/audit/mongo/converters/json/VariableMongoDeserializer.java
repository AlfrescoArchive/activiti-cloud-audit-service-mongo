package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.runtime.api.model.impl.VariableInstanceImpl;

public class VariableMongoDeserializer extends MongoDeserializer<VariableInstanceImpl> {

    public VariableMongoDeserializer() {
        super(VariableInstanceImpl.class);
    }
}
