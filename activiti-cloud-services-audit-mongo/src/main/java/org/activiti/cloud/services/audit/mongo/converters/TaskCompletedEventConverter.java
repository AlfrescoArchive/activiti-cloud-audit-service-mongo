package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.task.model.events.TaskRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.task.model.events.CloudTaskCompletedEvent;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCompletedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.events.TaskCompletedEventDocument;
import org.springframework.stereotype.Component;

@Component
public class TaskCompletedEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return TaskRuntimeEvent.TaskEvents.TASK_COMPLETED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudTaskCompletedEvent cloudTaskCompletedEvent = (CloudTaskCompletedEvent) cloudRuntimeEvent;
        TaskCompletedEventDocument taskCompletedEventEntity = new TaskCompletedEventDocument(cloudTaskCompletedEvent.getId(),
                                                                                             cloudTaskCompletedEvent.getTimestamp(),
                                                                                             cloudTaskCompletedEvent.getAppName(),
                                                                                             cloudTaskCompletedEvent.getAppVersion(),
                                                                                             cloudTaskCompletedEvent.getServiceFullName(),
                                                                                             cloudTaskCompletedEvent.getServiceName(),
                                                                                             cloudTaskCompletedEvent.getServiceType(),
                                                                                             cloudTaskCompletedEvent.getServiceVersion(),
                                                                                             cloudTaskCompletedEvent.getEntity());
        return taskCompletedEventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        TaskCompletedEventDocument taskCompletedEventEntity = (TaskCompletedEventDocument) auditEventEntity;

        CloudTaskCompletedEventImpl cloudTaskCompletedEvent = new CloudTaskCompletedEventImpl(taskCompletedEventEntity.getEventId(),
                                                                                              taskCompletedEventEntity.getTimestamp(),
                                                                                              taskCompletedEventEntity.getTask());
        cloudTaskCompletedEvent.setAppName(taskCompletedEventEntity.getAppName());
        cloudTaskCompletedEvent.setAppVersion(taskCompletedEventEntity.getAppVersion());
        cloudTaskCompletedEvent.setServiceFullName(taskCompletedEventEntity.getServiceFullName());
        cloudTaskCompletedEvent.setServiceName(taskCompletedEventEntity.getServiceName());
        cloudTaskCompletedEvent.setServiceType(taskCompletedEventEntity.getServiceType());
        cloudTaskCompletedEvent.setServiceVersion(taskCompletedEventEntity.getServiceVersion());

        return cloudTaskCompletedEvent;
    }
}
