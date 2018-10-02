package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.process.model.BPMNElement;
import org.activiti.api.process.model.SequenceFlow;
import org.activiti.cloud.services.audit.mongo.converters.json.SequenceMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.SequenceMongoSerializer;
import org.activiti.runtime.api.model.impl.SequenceFlowImpl;
import org.junit.Test;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.activiti.test.Assertions.assertThat;

public class SequenceFlowMongoJsonConverterTest {

    SequenceMongoSerializer sequenceMongoSerializer = new SequenceMongoSerializer();
    SequenceMongoDeserializer sequenceMongoDeserializer = new SequenceMongoDeserializer();

    @Test
    public void convertToDatabaseColumnShouldReturnTheEntityJsonRepresentation() throws Exception {
        //given
        SequenceFlowImpl sequenceFlow = new SequenceFlowImpl("source-element-id",
                                                             "target-element-id");

        sequenceFlow.setSourceActivityName("source-activity-name");
        sequenceFlow.setSourceActivityType("source-activity-type");
        sequenceFlow.setTargetActivityName("target-activity-name");
        sequenceFlow.setTargetActivityType("target-activity-type");
        sequenceFlow.setProcessDefinitionId("proc-def-id");
        sequenceFlow.setProcessInstanceId("proc-inst-id");
        //when
        String jsonRepresentation = sequenceMongoSerializer.serialize(sequenceFlow);

        //then
        assertThatJson(jsonRepresentation)
                .node("sourceActivityElementId").isEqualTo("source-element-id")
                .node("sourceActivityName").isEqualTo("source-activity-name")
                .node("sourceActivityType").isEqualTo("source-activity-type")
                .node("targetActivityElementId").isEqualTo("target-element-id")
                .node("targetActivityName").isEqualTo("target-activity-name")
                .node("targetActivityType").isEqualTo("target-activity-type")
                .node("processDefinitionId").isEqualTo("proc-def-id")
                .node("processInstanceId").isEqualTo("proc-inst-id");
    }

    @Test
    public void convertToEntityAttributeShouldCreateAProcessInstanceWithFieldsSet() throws Exception {
        //given
        String jsonRepresentation =
                "{\"sourceActivityElementId\":\"source-element-id\"," +
                        "\"sourceActivityName\":\"source-activity-name\"," +
                        "\"sourceActivityType\":\"source-activity-type\"," +
                        "\"targetActivityElementId\":\"target-element-id\"," +
                        "\"targetActivityName\":\"target-activity-name\"," +
                        "\"targetActivityType\":\"target-activity-type\"," +
                        "\"processDefinitionId\":\"proc-def-id\"," +
                        "\"processInstanceId\":\"proc-inst-id\"}";

        //when
        SequenceFlow sequenceFlow = sequenceMongoDeserializer.deserialize(jsonRepresentation);

        //then
        assertThat(sequenceFlow)
                .isNotNull()
                .hasSourceActivityElementId("source-element-id")
                .hasSourceActivityName("source-activity-name")
                .hasSourceActivityType("source-activity-type")
                .hasTargetActivityElementId("target-element-id")
                .hasTargetActivityName("target-activity-name")
                .hasTargetActivityType("target-activity-type");

        assertThat((BPMNElement) sequenceFlow)
                .hasProcessDefinitionId("proc-def-id")
                .hasProcessInstanceId("proc-inst-id");
    }
}
