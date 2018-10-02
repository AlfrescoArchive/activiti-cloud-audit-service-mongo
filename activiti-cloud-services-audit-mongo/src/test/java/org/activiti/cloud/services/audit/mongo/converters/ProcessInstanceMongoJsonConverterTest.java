package org.activiti.cloud.services.audit.mongo.converters;

import java.util.Date;

import org.activiti.api.process.model.ProcessInstance;
import org.activiti.cloud.services.audit.mongo.converters.json.ProcessMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.ProcessMongoSerializer;
import org.activiti.runtime.api.model.impl.ProcessInstanceImpl;
import org.junit.Test;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.activiti.test.Assertions.assertThat;

public class ProcessInstanceMongoJsonConverterTest {

    ProcessMongoSerializer processMongoSerializer = new ProcessMongoSerializer();
    ProcessMongoDeserializer processMongoDeserializer = new ProcessMongoDeserializer();

    @Test
    public void convertToDatabaseColumnShouldReturnTheEntityJsonRepresentation() throws Exception {
        //given
        ProcessInstanceImpl processInstance = new ProcessInstanceImpl();
        processInstance.setId("20");
        processInstance.setName("My instance");
        processInstance.setDescription("This is my process instance");
        processInstance.setProcessDefinitionId("proc-def-id");
        processInstance.setInitiator("initiator");
        processInstance.setStartDate(new Date());
        processInstance.setBusinessKey("business-key");
        processInstance.setStatus(ProcessInstance.ProcessInstanceStatus.RUNNING);



        //when
        String jsonRepresentation = processMongoSerializer.serialize(processInstance);

        //then
        assertThatJson(jsonRepresentation)
                .node("name").isEqualTo("My instance")
                .node("status").isEqualTo("RUNNING")
                .node("processDefinitionId").isEqualTo("proc-def-id")
                .node("businessKey").isEqualTo("business-key")
                .node("id").isEqualTo("\"20\"");
    }

    @Test
    public void convertToEntityAttributeShouldCreateAProcessInstanceWithFieldsSet() throws Exception {
        //given
        String jsonRepresentation =
                "{\"id\":\"20\"," +
                        "\"status\":\"RUNNING\"," +
                        "\"name\":\"My instance\"," +
                        "\"processDefinitionId\":\"proc-def-id\"}";

        //when
        ProcessInstance processInstance = processMongoDeserializer.deserialize(jsonRepresentation);

        //then
        assertThat(processInstance)
                .isNotNull()
                .hasId("20")
                .hasStatus(ProcessInstance.ProcessInstanceStatus.RUNNING)
                .hasProcessDefinitionId("proc-def-id");
    }
}
