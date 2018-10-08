package org.activiti.cloud.services.audit.mongo.converters;

import java.io.Serializable;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.cloud.services.audit.mongo.converters.json.VariableMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.VariableMongoSerializer;
import org.activiti.runtime.api.model.impl.VariableInstanceImpl;
import org.junit.Test;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.activiti.test.Assertions.assertThat;

public class VariablesMongoJsonConverterTest {

    VariableMongoSerializer variableMongoSerializer = new VariableMongoSerializer();
    VariableMongoDeserializer variableMongoDeserializer = new VariableMongoDeserializer();

    @Test
    public void convertToDatabaseColumnShouldReturnTheEntityJsonRepresentation() throws Exception {
        //given
        VariableInstanceImpl variable = new VariableInstanceImpl("var-name",
                                                                 "String",
                                                                 "my string value",
                                                                 "proc-inst-id");
        variable.setTaskId("task-id");

        //when
        String jsonRepresentation = variableMongoSerializer.serialize(variable);

        //then
        assertThatJson(jsonRepresentation)
                .node("name").isEqualTo("var-name")
                .node("type").isEqualTo("String")
                .node("value").isEqualTo("my string value")
                .node("taskId").isEqualTo("task-id")
                .node("processInstanceId").isEqualTo("proc-inst-id");
    }

    @Test
    public void convertToEntityAttributeShouldCreateAProcessInstanceWithFieldsSet() throws Exception {
        //given
        String jsonRepresentation =
                "{\"name\":\"var-name\"," +
                        "\"type\":\"String\"," +
                        "\"value\":\"my string value\"," +
                        "\"taskId\":\"task-id\"," +
                        "\"processInstanceId\":\"proc-inst-id\"}";

        //when
        VariableInstance variableInstance = variableMongoDeserializer.deserialize(jsonRepresentation);

        //then
        assertThat(variableInstance)
                .isNotNull()
                .hasType("String")
                .hasName("var-name")
                .hasValue("my string value")
                .hasProcessInstanceId("proc-inst-id")
                .hasTaskId("task-id");
    }

    @Test
    public void converterShouldDealWithDifferentTypes() throws Exception {

        Invoice invoice = new Invoice("inv-id", "customer");
        //given
        VariableInstanceImpl variable = new VariableInstanceImpl("var-name",
                                                                 "Invoice",
                                                                 invoice,
                                                                 "proc-inst-id");
        variable.setTaskId("task-id");

        //when
        String jsonRepresentation = variableMongoSerializer.serialize(variable);

        //then
        assertThatJson(jsonRepresentation)
                .node("name").isEqualTo("var-name")
                .node("type").isEqualTo("Invoice")
                .node("value").isEqualTo(invoice)
                .node("taskId").isEqualTo("task-id")
                .node("processInstanceId").isEqualTo("proc-inst-id");
    }



    private class Invoice implements Serializable{
        private String id;
        private String customer;

        public Invoice() {
        }

        public Invoice(String id,
                       String customer) {
            this.id = id;
            this.customer = customer;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }
    }
}
