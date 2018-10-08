package org.activiti.cloud.services.audit.mongo.converters.json;

import org.activiti.runtime.api.model.impl.VariableInstanceImpl;

public class VariableMongoDeserializer extends MongoDeserializer<VariableInstanceImpl> {

    private static final long serialVersionUID = 3602737382512291821L;

    public VariableMongoDeserializer() {
        super(VariableInstanceImpl.class);
    }
}
