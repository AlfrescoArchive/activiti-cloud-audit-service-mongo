package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.process.model.events.ProcessRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudProcessCompletedEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessCompletedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.events.ProcessCompletedEventDocument;
import org.springframework.stereotype.Component;

@Component
public class ProcessCompletedEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return ProcessRuntimeEvent.ProcessEvents.PROCESS_COMPLETED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudProcessCompletedEvent cloudProcessCompletedEvent = (CloudProcessCompletedEvent) cloudRuntimeEvent;
        ProcessCompletedEventDocument processCompletedEventEntity = new ProcessCompletedEventDocument(cloudProcessCompletedEvent.getId(),
                                                                                                      cloudProcessCompletedEvent.getTimestamp(),
                                                                                                      cloudProcessCompletedEvent.getAppName(),
                                                                                                      cloudProcessCompletedEvent.getAppVersion(),
                                                                                                      cloudProcessCompletedEvent.getServiceFullName(),
                                                                                                      cloudProcessCompletedEvent.getServiceName(),
                                                                                                      cloudProcessCompletedEvent.getServiceType(),
                                                                                                      cloudProcessCompletedEvent.getServiceVersion(),
                                                                                                      cloudProcessCompletedEvent.getEntity());

        return processCompletedEventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        ProcessCompletedEventDocument processCompletedEventEntity = (ProcessCompletedEventDocument) auditEventEntity;
        CloudProcessCompletedEventImpl cloudProcessCompletedEvent = new CloudProcessCompletedEventImpl(processCompletedEventEntity.getEventId(),
                                                                                                       processCompletedEventEntity.getTimestamp(),
                                                                                                       processCompletedEventEntity.getProcessInstance());
        cloudProcessCompletedEvent.setAppName(processCompletedEventEntity.getAppName());
        cloudProcessCompletedEvent.setAppVersion(processCompletedEventEntity.getAppVersion());
        cloudProcessCompletedEvent.setServiceFullName(processCompletedEventEntity.getServiceFullName());
        cloudProcessCompletedEvent.setServiceName(processCompletedEventEntity.getServiceName());
        cloudProcessCompletedEvent.setServiceType(processCompletedEventEntity.getServiceType());
        cloudProcessCompletedEvent.setServiceVersion(processCompletedEventEntity.getServiceVersion());
        return cloudProcessCompletedEvent;
    }
}
