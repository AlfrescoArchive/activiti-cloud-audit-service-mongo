package org.activiti.cloud.services.audit.mongo.converters.json;

import java.io.IOException;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.BPMNActivity;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.SequenceFlow;
import org.activiti.api.task.model.Task;
import org.activiti.cloud.services.audit.api.AuditException;
import org.activiti.runtime.api.model.impl.BPMNActivityImpl;
import org.activiti.runtime.api.model.impl.ProcessInstanceImpl;
import org.activiti.runtime.api.model.impl.SequenceFlowImpl;
import org.activiti.runtime.api.model.impl.TaskImpl;
import org.activiti.runtime.api.model.impl.VariableInstanceImpl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class MongoSerializer<T> extends StdSerializer<T>{

    private static final long serialVersionUID = 8321567206583190124L;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        {
            SimpleModule module = new SimpleModule("mapCommonModelInterfaces",
                                                   Version.unknownVersion());
            SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver() {
                private static final long serialVersionUID = -5067983104871707780L;

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

    public MongoSerializer(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeRawValue((serialize(value)));
        } catch (JsonProcessingException e) {
            throw new AuditException("Unable to serialize object.",
                                     e);
        }
    }

    public String serialize(T value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
