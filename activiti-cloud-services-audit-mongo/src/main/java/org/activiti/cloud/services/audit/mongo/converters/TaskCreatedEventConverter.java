package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.task.model.events.TaskRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.task.model.events.CloudTaskCreatedEvent;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCreatedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.events.TaskCreatedEventDocument;
import org.springframework.stereotype.Component;

@Component
public class TaskCreatedEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return TaskRuntimeEvent.TaskEvents.TASK_CREATED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudTaskCreatedEvent cloudTaskCreatedEvent = (CloudTaskCreatedEvent) cloudRuntimeEvent;
        TaskCreatedEventDocument taskCreatedEventEntity = new TaskCreatedEventDocument(cloudTaskCreatedEvent.getId(),
                                                                                       cloudTaskCreatedEvent.getTimestamp(),
                                                                                       cloudTaskCreatedEvent.getAppName(),
                                                                                       cloudTaskCreatedEvent.getAppVersion(),
                                                                                       cloudTaskCreatedEvent.getServiceFullName(),
                                                                                       cloudTaskCreatedEvent.getServiceName(),
                                                                                       cloudTaskCreatedEvent.getServiceType(),
                                                                                       cloudTaskCreatedEvent.getServiceVersion(),
                                                                                       cloudTaskCreatedEvent.getEntity());
        return taskCreatedEventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        TaskCreatedEventDocument taskCreatedEventEntity = (TaskCreatedEventDocument) auditEventEntity;

        CloudTaskCreatedEventImpl cloudTaskCreatedEvent = new CloudTaskCreatedEventImpl(taskCreatedEventEntity.getEventId(),
                                                                                        taskCreatedEventEntity.getTimestamp(),
                                                                                        taskCreatedEventEntity.getTask());
        cloudTaskCreatedEvent.setAppName(taskCreatedEventEntity.getAppName());
        cloudTaskCreatedEvent.setAppVersion(taskCreatedEventEntity.getAppVersion());
        cloudTaskCreatedEvent.setServiceFullName(taskCreatedEventEntity.getServiceFullName());
        cloudTaskCreatedEvent.setServiceName(taskCreatedEventEntity.getServiceName());
        cloudTaskCreatedEvent.setServiceType(taskCreatedEventEntity.getServiceType());
        cloudTaskCreatedEvent.setServiceVersion(taskCreatedEventEntity.getServiceVersion());

        return cloudTaskCreatedEvent;
    }
}
