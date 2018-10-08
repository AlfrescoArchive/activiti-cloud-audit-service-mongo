package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.process.model.events.ProcessRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudProcessStartedEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessStartedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.events.ProcessStartedEventDocument;
import org.springframework.stereotype.Component;

@Component
public class ProcessStartedEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return ProcessRuntimeEvent.ProcessEvents.PROCESS_STARTED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudProcessStartedEvent cloudProcessStartedEvent = (CloudProcessStartedEvent) cloudRuntimeEvent;
        ProcessStartedEventDocument processStartedEventEntity = new ProcessStartedEventDocument(cloudProcessStartedEvent.getId(),
                                                                                                cloudProcessStartedEvent.getTimestamp(),
                                                                                                cloudProcessStartedEvent.getAppName(),
                                                                                                cloudProcessStartedEvent.getAppVersion(),
                                                                                                cloudProcessStartedEvent.getServiceFullName(),
                                                                                                cloudProcessStartedEvent.getServiceName(),
                                                                                                cloudProcessStartedEvent.getServiceType(),
                                                                                                cloudProcessStartedEvent.getServiceVersion(),
                                                                                                cloudProcessStartedEvent.getEntity(),
                                                                                                cloudProcessStartedEvent.getNestedProcessDefinitionId(),
                                                                                                cloudProcessStartedEvent.getNestedProcessInstanceId());

        return processStartedEventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        ProcessStartedEventDocument processStartedAuditEventDocument = (ProcessStartedEventDocument) auditEventEntity;
        CloudProcessStartedEventImpl cloudProcessStartedEvent = new CloudProcessStartedEventImpl(processStartedAuditEventDocument.getEventId(),
                                                                                                 processStartedAuditEventDocument.getTimestamp(),
                                                                                                 processStartedAuditEventDocument.getProcessInstance());
        cloudProcessStartedEvent.setAppName(processStartedAuditEventDocument.getAppName());
        cloudProcessStartedEvent.setAppVersion(processStartedAuditEventDocument.getAppVersion());
        cloudProcessStartedEvent.setServiceFullName(processStartedAuditEventDocument.getServiceFullName());
        cloudProcessStartedEvent.setServiceName(processStartedAuditEventDocument.getServiceName());
        cloudProcessStartedEvent.setServiceType(processStartedAuditEventDocument.getServiceType());
        cloudProcessStartedEvent.setServiceVersion(processStartedAuditEventDocument.getServiceVersion());
        return cloudProcessStartedEvent;
    }
}
