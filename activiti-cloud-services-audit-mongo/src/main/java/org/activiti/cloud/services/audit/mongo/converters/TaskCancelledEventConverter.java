package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.task.model.events.TaskRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.task.model.events.CloudTaskCancelledEvent;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCancelledEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.events.TaskCancelledEventDocument;
import org.springframework.stereotype.Component;

@Component
public class TaskCancelledEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return TaskRuntimeEvent.TaskEvents.TASK_CANCELLED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudTaskCancelledEvent cloudTaskCancelledEvent = (CloudTaskCancelledEvent) cloudRuntimeEvent;
        TaskCancelledEventDocument taskCancelledEventEntity = new TaskCancelledEventDocument(cloudTaskCancelledEvent.getId(),
                                                                                             cloudTaskCancelledEvent.getTimestamp(),
                                                                                             cloudTaskCancelledEvent.getAppName(),
                                                                                             cloudTaskCancelledEvent.getAppVersion(),
                                                                                             cloudTaskCancelledEvent.getServiceFullName(),
                                                                                             cloudTaskCancelledEvent.getServiceName(),
                                                                                             cloudTaskCancelledEvent.getServiceType(),
                                                                                             cloudTaskCancelledEvent.getServiceVersion(),
                                                                                             cloudTaskCancelledEvent.getEntity(),
                                                                                             cloudTaskCancelledEvent.getCause());
        taskCancelledEventEntity.setProcessDefinitionId((cloudTaskCancelledEvent.getEntity() != null) ? cloudTaskCancelledEvent.getEntity()
                                                                                                                               .getProcessDefinitionId() : null);
        taskCancelledEventEntity.setProcessInstanceId((cloudTaskCancelledEvent.getEntity() != null) ? cloudTaskCancelledEvent.getEntity()
                                                                                                                             .getProcessInstanceId() : null);
        taskCancelledEventEntity.setEntityId(cloudTaskCancelledEvent.getEntityId());
        return taskCancelledEventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        TaskCancelledEventDocument taskCancelledEventEntity = (TaskCancelledEventDocument) auditEventEntity;

        CloudTaskCancelledEventImpl cloudTaskCancelledEvent = new CloudTaskCancelledEventImpl(taskCancelledEventEntity.getEventId(),
                                                                                              taskCancelledEventEntity.getTimestamp(),
                                                                                              taskCancelledEventEntity.getTask());
        cloudTaskCancelledEvent.setAppName(taskCancelledEventEntity.getAppName());
        cloudTaskCancelledEvent.setAppVersion(taskCancelledEventEntity.getAppVersion());
        cloudTaskCancelledEvent.setServiceFullName(taskCancelledEventEntity.getServiceFullName());
        cloudTaskCancelledEvent.setServiceName(taskCancelledEventEntity.getServiceName());
        cloudTaskCancelledEvent.setServiceType(taskCancelledEventEntity.getServiceType());
        cloudTaskCancelledEvent.setServiceVersion(taskCancelledEventEntity.getServiceVersion());
        cloudTaskCancelledEvent.setCause(taskCancelledEventEntity.getCause());

        return cloudTaskCancelledEvent;
    }
}
