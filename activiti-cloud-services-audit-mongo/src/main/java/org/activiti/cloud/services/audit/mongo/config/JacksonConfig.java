package org.activiti.cloud.services.audit.mongo.config;

import org.activiti.runtime.api.model.impl.TaskImpl;
import org.activiti.cloud.services.audit.mongo.converters.json.TaskMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.TaskMongoSerializer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

@Component
public class JacksonConfig extends SimpleModule {

    /**
     * 
     */
    private static final long serialVersionUID = -5022833438563372295L;

    @Override public void setupModule(SetupContext context) {
        SimpleSerializers simpleSerializers = new SimpleSerializers();
        simpleSerializers.addSerializer(new TaskMongoSerializer());
        context.addSerializers(simpleSerializers);
        SimpleDeserializers simpleDeSerializers = new SimpleDeserializers();
        simpleDeSerializers.addDeserializer(TaskImpl.class, new TaskMongoDeserializer());
        context.addDeserializers(simpleDeSerializers);
    }
}
