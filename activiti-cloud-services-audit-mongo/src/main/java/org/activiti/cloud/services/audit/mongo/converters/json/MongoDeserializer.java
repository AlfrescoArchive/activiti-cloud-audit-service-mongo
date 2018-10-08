package org.activiti.cloud.services.audit.mongo.converters.json;

import java.io.IOException;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.BPMNActivity;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.SequenceFlow;
import org.activiti.api.task.model.Task;
import org.activiti.runtime.api.model.impl.BPMNActivityImpl;
import org.activiti.runtime.api.model.impl.ProcessInstanceImpl;
import org.activiti.runtime.api.model.impl.SequenceFlowImpl;
import org.activiti.runtime.api.model.impl.TaskImpl;
import org.activiti.runtime.api.model.impl.VariableInstanceImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MongoDeserializer<T> extends StdDeserializer<T>{

    private static final long serialVersionUID = 6716934799936856568L;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        {
            SimpleModule module = new SimpleModule("mapCommonModelInterfaces",
                                                   Version.unknownVersion());
            SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver() {
                private static final long serialVersionUID = 2379507897444040983L;

                //this is a workaround for https://github.com/FasterXML/jackson-databind/issues/2019
                //once version 2.9.6 is related we can remove this @override method
                @Override
                public JavaType resolveAbstractType(DeserializationConfig config,
                                                    BeanDescription typeDesc) {
                    return findTypeMapping(config,
                                           typeDesc.getType());
                }
            };

            resolver.addMapping(VariableInstance.class,
                                VariableInstanceImpl.class);
            resolver.addMapping(ProcessInstance.class,
                                ProcessInstanceImpl.class);

            resolver.addMapping(Task.class,
                                TaskImpl.class);
            resolver.addMapping(BPMNActivity.class,
                                BPMNActivityImpl.class);
            resolver.addMapping(SequenceFlow.class,
                                SequenceFlowImpl.class);
            module.setAbstractTypes(resolver);

            objectMapper.registerModule(module);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    }

    private Class<T> entityClass;

    public MongoDeserializer(Class<T> entityClass) {
        super(entityClass);
        this.entityClass = entityClass;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return deserialize(p.getValueAsString());
    }

    public T deserialize(String json) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(json, entityClass);
    }
}
