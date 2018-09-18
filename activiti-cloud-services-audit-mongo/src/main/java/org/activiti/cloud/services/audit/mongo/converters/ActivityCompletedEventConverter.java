package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.process.model.events.BPMNActivityEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNActivityCompletedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.ActivityCompletedEventDocument;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.springframework.stereotype.Component;

@Component
public class ActivityCompletedEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return BPMNActivityEvent.ActivityEvents.ACTIVITY_COMPLETED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudBPMNActivityCompletedEventImpl cloudBPMNActivityCompletedEvent = (CloudBPMNActivityCompletedEventImpl) cloudRuntimeEvent;
        ActivityCompletedEventDocument activityCompletedAuditEventDocument = new ActivityCompletedEventDocument(cloudBPMNActivityCompletedEvent.getId(),
                                                                                                                cloudBPMNActivityCompletedEvent.getTimestamp(),
                                                                                                                cloudBPMNActivityCompletedEvent.getAppName(),
                                                                                                                cloudBPMNActivityCompletedEvent.getAppVersion(),
                                                                                                                cloudBPMNActivityCompletedEvent.getServiceFullName(),
                                                                                                                cloudBPMNActivityCompletedEvent.getServiceName(),
                                                                                                                cloudBPMNActivityCompletedEvent.getServiceType(),
                                                                                                                cloudBPMNActivityCompletedEvent.getServiceVersion(),
                                                                                                                cloudBPMNActivityCompletedEvent.getEntity());
        activityCompletedAuditEventDocument.setEntityId(cloudBPMNActivityCompletedEvent.getProcessInstanceId());
        activityCompletedAuditEventDocument.setProcessDefinitionId(cloudBPMNActivityCompletedEvent.getProcessDefinitionId());
        activityCompletedAuditEventDocument.setProcessInstanceId(cloudBPMNActivityCompletedEvent.getProcessInstanceId());
        return activityCompletedAuditEventDocument;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        ActivityCompletedEventDocument activityCompletedAuditEventDocument = (ActivityCompletedEventDocument) auditEventEntity;

        CloudBPMNActivityCompletedEventImpl bpmnActivityCompletedEvent = new CloudBPMNActivityCompletedEventImpl(activityCompletedAuditEventDocument.getEventId(),
                                                                                                                 activityCompletedAuditEventDocument.getTimestamp(),
                                                                                                                 activityCompletedAuditEventDocument.getBpmnActivity(),
                                                                                                                 activityCompletedAuditEventDocument.getProcessDefinitionId(),
                                                                                                                 activityCompletedAuditEventDocument.getProcessInstanceId());
        bpmnActivityCompletedEvent.setAppName(activityCompletedAuditEventDocument.getAppName());
        bpmnActivityCompletedEvent.setAppVersion(activityCompletedAuditEventDocument.getAppVersion());
        bpmnActivityCompletedEvent.setServiceFullName(activityCompletedAuditEventDocument.getServiceFullName());
        bpmnActivityCompletedEvent.setServiceName(activityCompletedAuditEventDocument.getServiceName());
        bpmnActivityCompletedEvent.setServiceType(activityCompletedAuditEventDocument.getServiceType());
        bpmnActivityCompletedEvent.setServiceVersion(activityCompletedAuditEventDocument.getServiceVersion());
        bpmnActivityCompletedEvent.setProcessDefinitionId(activityCompletedAuditEventDocument.getProcessDefinitionId());
        bpmnActivityCompletedEvent.setProcessInstanceId(activityCompletedAuditEventDocument.getProcessInstanceId());
        return bpmnActivityCompletedEvent;
    }
}
