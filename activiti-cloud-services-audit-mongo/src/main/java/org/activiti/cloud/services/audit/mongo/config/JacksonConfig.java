package org.activiti.cloud.services.audit.mongo.config;

import org.activiti.runtime.api.model.impl.BPMNActivityImpl;
import org.activiti.runtime.api.model.impl.ProcessInstanceImpl;
import org.activiti.runtime.api.model.impl.SequenceFlowImpl;
import org.activiti.runtime.api.model.impl.TaskImpl;
import org.activiti.runtime.api.model.impl.VariableInstanceImpl;
import org.activiti.cloud.services.audit.mongo.converters.json.ActivityMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.ActivityMongoSerializer;
import org.activiti.cloud.services.audit.mongo.converters.json.ProcessMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.ProcessMongoSerializer;
import org.activiti.cloud.services.audit.mongo.converters.json.SequenceMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.SequenceMongoSerializer;
import org.activiti.cloud.services.audit.mongo.converters.json.TaskMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.TaskMongoSerializer;
import org.activiti.cloud.services.audit.mongo.converters.json.VariableMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.VariableMongoSerializer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

@Component
public class JacksonConfig extends SimpleModule {

    private static final long serialVersionUID = -5022833438563372295L;

    @Override public void setupModule(SetupContext context) {
        SimpleSerializers simpleSerializers = new SimpleSerializers();
        simpleSerializers.addSerializer(new ActivityMongoSerializer());
        simpleSerializers.addSerializer(new ProcessMongoSerializer());
        simpleSerializers.addSerializer(new SequenceMongoSerializer());
        simpleSerializers.addSerializer(new TaskMongoSerializer());
        simpleSerializers.addSerializer(new VariableMongoSerializer());
        context.addSerializers(simpleSerializers);
        SimpleDeserializers simpleDeSerializers = new SimpleDeserializers();
        simpleDeSerializers.addDeserializer(BPMNActivityImpl.class, new ActivityMongoDeserializer());
        simpleDeSerializers.addDeserializer(ProcessInstanceImpl.class, new ProcessMongoDeserializer());
        simpleDeSerializers.addDeserializer(SequenceFlowImpl.class, new SequenceMongoDeserializer());
        simpleDeSerializers.addDeserializer(TaskImpl.class, new TaskMongoDeserializer());
        simpleDeSerializers.addDeserializer(VariableInstanceImpl.class, new VariableMongoDeserializer());
        context.addDeserializers(simpleDeSerializers);
    }
}
