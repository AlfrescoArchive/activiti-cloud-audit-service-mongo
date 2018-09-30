package org.activiti.cloud.services.audit.mongo.converters;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.activiti.test.Assertions.assertThat;

import org.activiti.api.task.model.Task;
import org.activiti.cloud.services.audit.mongo.converters.json.TaskMongoDeserializer;
import org.activiti.cloud.services.audit.mongo.converters.json.TaskMongoSerializer;
import org.activiti.runtime.api.model.impl.TaskImpl;
import org.junit.Test;

public class TaskMongoJsonConverterTest {

    private TaskMongoSerializer serializer = new TaskMongoSerializer();
    private TaskMongoDeserializer deserializer = new TaskMongoDeserializer();

    @Test
    public void convertToDatabaseColumnShouldReturnTheEntityJsonRepresentation() throws Exception {
        //given
        TaskImpl task = new TaskImpl("3", "task1",
                                     Task.TaskStatus.CREATED);
        task.setAssignee("user1");
        task.setDescription("First task");
        task.setOwner("user2");
        task.setPriority(50);
        task.setProcessDefinitionId("proc-def-id");
        task.setProcessInstanceId("10");
        task.setParentTaskId("parent-task-id");


        //when
        String jsonRepresentation = serializer.serialize(task);

        //then
        assertThatJson(jsonRepresentation)
                .node("id").isEqualTo("\"3\"")
                .node("name").isEqualTo("task1")
                .node("processDefinitionId").isEqualTo("proc-def-id")
                .node("assignee").isEqualTo("user1")
                .node("description").isEqualTo("First task")
                .node("owner").isEqualTo("user2")
                .node("priority").isEqualTo("50")
                .node("processInstanceId").isEqualTo("\"10\"")
                .node("parentTaskId").isEqualTo("parent-task-id");
    }

    @Test
    public void convertToEntityAttributeShouldCreatedATaskInstanceWithFieldsSet() throws Exception {
        //given
        String jsonRepresentation =
                "{\"id\":\"3\"," +
                        "\"owner\":\"user2\"," +
                        "\"assignee\":\"user1\"," +
                        "\"name\":\"task1\"," +
                        "\"description\":\"First task\"," +
                        "\"priority\":50," +
                        "\"parentTaskId\":\"parent-task-id\"," +
                        "\"processDefinitionId\":\"proc-def-id\"," +
                        "\"processInstanceId\":\"10\"}";

        //when
        Task task = deserializer.deserialize(jsonRepresentation);

        //then
        assertThat(task)
                .isNotNull()
                .hasId("3")
                .hasOwner("user2")
                .hasAssignee("user1")
                .hasName("task1")
                .hasDescription("First task")
                .hasPriority(50)
                .hasParentTaskId("parent-task-id")
                .hasProcessDefinitionId("proc-def-id")
                .hasProcessInstanceId("10");
    }
}