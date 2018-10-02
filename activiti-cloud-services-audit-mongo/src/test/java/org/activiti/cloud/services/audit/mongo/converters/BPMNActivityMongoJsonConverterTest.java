package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.process.model.BPMNActivity;
import org.activiti.api.process.model.BPMNElement;
import org.activiti.cloud.services.audit.mongo.converters.json.ActivityMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.ActivityMongoSerializer;
import org.activiti.runtime.api.model.impl.BPMNActivityImpl;
import org.junit.Test;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.activiti.test.Assertions.assertThat;

public class BPMNActivityMongoJsonConverterTest {

    ActivityMongoSerializer activityMongoSerializer = new ActivityMongoSerializer();
    ActivityMongoDeserializer activityMongoDeserializer = new ActivityMongoDeserializer();

    @Test
    public void convertToDatabaseColumnShouldReturnTheEntityJsonRepresentation() throws Exception {
        //given
        BPMNActivityImpl bpmnActivity = new BPMNActivityImpl("element-id",
                                                             "BPMN Activity Name",
                                                             "Service Task");
        bpmnActivity.setProcessDefinitionId("proc-def-id");
        bpmnActivity.setProcessInstanceId("proc-inst-id");

        //when
        String jsonRepresentation = activityMongoSerializer.serialize(bpmnActivity);

        //then
        assertThatJson(jsonRepresentation)
                .node("elementId").isEqualTo("element-id")
                .node("activityName").isEqualTo("BPMN Activity Name")
                .node("activityType").isEqualTo("Service Task")
                .node("processDefinitionId").isEqualTo("proc-def-id")
                .node("processInstanceId").isEqualTo("proc-inst-id");
    }

    @Test
    public void convertToEntityAttributeShouldCreateAProcessInstanceWithFieldsSet() throws Exception {
        //given
        String jsonRepresentation =
                "{\"elementId\":\"element-id\"," +
                        "\"activityName\":\"BPMN Activity Name\"," +
                        "\"activityType\":\"Service Task\"," +
                        "\"processDefinitionId\":\"proc-def-id\"," +
                        "\"processInstanceId\":\"proc-inst-id\"}";

        //when
        BPMNActivity bpmnActivity = activityMongoDeserializer.deserialize(jsonRepresentation);

        //then
        assertThat(bpmnActivity)
                .isNotNull()
                .hasActivityName("BPMN Activity Name")
                .hasActivityType("Service Task")
                .hasElementId("element-id");
        assertThat((BPMNElement)bpmnActivity)
                .hasProcessInstanceId("proc-inst-id")
                .hasProcessDefinitionId("proc-def-id");
    }
}
