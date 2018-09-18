package org.activiti.cloud.services.audit.mongo.converters;


import org.activiti.api.process.model.events.ProcessRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudProcessCancelledEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessCancelledEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.events.ProcessCancelledEventDocument;
import org.springframework.stereotype.Component;

@Component
public class ProcessCancelledEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return ProcessRuntimeEvent.ProcessEvents.PROCESS_CANCELLED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudProcessCancelledEvent cloudProcessCancelledEvent = (CloudProcessCancelledEvent) cloudRuntimeEvent;
        ProcessCancelledEventDocument processCancelledEventEntity = new ProcessCancelledEventDocument(cloudProcessCancelledEvent.getId(),
                                                                                                      cloudProcessCancelledEvent.getTimestamp(),
                                                                                                      cloudProcessCancelledEvent.getCause());
        processCancelledEventEntity.setAppName(cloudProcessCancelledEvent.getAppName());
        processCancelledEventEntity.setAppVersion(cloudProcessCancelledEvent.getAppVersion());
        processCancelledEventEntity.setServiceFullName(cloudProcessCancelledEvent.getServiceFullName());
        processCancelledEventEntity.setServiceName(cloudProcessCancelledEvent.getServiceName());
        processCancelledEventEntity.setServiceType(cloudProcessCancelledEvent.getServiceType());
        processCancelledEventEntity.setServiceVersion(cloudProcessCancelledEvent.getServiceVersion());
        processCancelledEventEntity.setProcessInstance(cloudProcessCancelledEvent.getEntity());
        processCancelledEventEntity.setProcessDefinitionId(cloudProcessCancelledEvent.getEntity().getProcessDefinitionId());
        processCancelledEventEntity.setProcessInstanceId(cloudProcessCancelledEvent.getEntity().getId());

        return processCancelledEventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        ProcessCancelledEventDocument processCancelledAuditEventDocument = (ProcessCancelledEventDocument) auditEventEntity;
        CloudProcessCancelledEventImpl cloudProcessCancelledEvent = new CloudProcessCancelledEventImpl(processCancelledAuditEventDocument.getEventId(),
                                                                                                       processCancelledAuditEventDocument.getTimestamp(),
                                                                                                       processCancelledAuditEventDocument.getProcessInstance());
        cloudProcessCancelledEvent.setAppName(processCancelledAuditEventDocument.getAppName());
        cloudProcessCancelledEvent.setAppVersion(processCancelledAuditEventDocument.getAppVersion());
        cloudProcessCancelledEvent.setServiceFullName(processCancelledAuditEventDocument.getServiceFullName());
        cloudProcessCancelledEvent.setServiceName(processCancelledAuditEventDocument.getServiceName());
        cloudProcessCancelledEvent.setServiceType(processCancelledAuditEventDocument.getServiceType());
        cloudProcessCancelledEvent.setServiceVersion(processCancelledAuditEventDocument.getServiceVersion());
        cloudProcessCancelledEvent.setCause(processCancelledAuditEventDocument.getCause());
        return cloudProcessCancelledEvent;
    }
}
