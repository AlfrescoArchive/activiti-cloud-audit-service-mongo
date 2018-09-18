package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.process.model.events.BPMNActivityEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNActivityCancelledEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.ActivityCancelledEventDocument;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.springframework.stereotype.Component;

@Component
public class ActivityCancelledEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return BPMNActivityEvent.ActivityEvents.ACTIVITY_CANCELLED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudBPMNActivityCancelledEventImpl cloudBPMNActivityCancelledEvent = (CloudBPMNActivityCancelledEventImpl) cloudRuntimeEvent;
        ActivityCancelledEventDocument activityCancelledAuditEventDocument = new ActivityCancelledEventDocument(cloudBPMNActivityCancelledEvent.getId(),
                                                                                                                cloudBPMNActivityCancelledEvent.getTimestamp(),
                                                                                                                cloudBPMNActivityCancelledEvent.getAppName(),
                                                                                                                cloudBPMNActivityCancelledEvent.getAppVersion(),
                                                                                                                cloudBPMNActivityCancelledEvent.getServiceFullName(),
                                                                                                                cloudBPMNActivityCancelledEvent.getServiceName(),
                                                                                                                cloudBPMNActivityCancelledEvent.getServiceType(),
                                                                                                                cloudBPMNActivityCancelledEvent.getServiceVersion(),
                                                                                                                cloudBPMNActivityCancelledEvent.getEntity(),
                                                                                                                cloudBPMNActivityCancelledEvent.getCause());
        activityCancelledAuditEventDocument.setEntityId(cloudBPMNActivityCancelledEvent.getProcessInstanceId());
        activityCancelledAuditEventDocument.setProcessDefinitionId(cloudBPMNActivityCancelledEvent.getProcessDefinitionId());
        activityCancelledAuditEventDocument.setProcessInstanceId(cloudBPMNActivityCancelledEvent.getProcessInstanceId());
        return activityCancelledAuditEventDocument;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        ActivityCancelledEventDocument activityCancelledAuditEventDocument = (ActivityCancelledEventDocument) auditEventEntity;

        CloudBPMNActivityCancelledEventImpl bpmnActivityCancelledEvent = new CloudBPMNActivityCancelledEventImpl(activityCancelledAuditEventDocument.getEventId(),
                                                                                                                 activityCancelledAuditEventDocument.getTimestamp(),
                                                                                                                 activityCancelledAuditEventDocument.getBpmnActivity(),
                                                                                                                 activityCancelledAuditEventDocument.getProcessDefinitionId(),
                                                                                                                 activityCancelledAuditEventDocument.getProcessInstanceId(),
                                                                                                                 activityCancelledAuditEventDocument.getCause());
        bpmnActivityCancelledEvent.setAppName(activityCancelledAuditEventDocument.getAppName());
        bpmnActivityCancelledEvent.setAppVersion(activityCancelledAuditEventDocument.getAppVersion());
        bpmnActivityCancelledEvent.setServiceFullName(activityCancelledAuditEventDocument.getServiceFullName());
        bpmnActivityCancelledEvent.setServiceName(activityCancelledAuditEventDocument.getServiceName());
        bpmnActivityCancelledEvent.setServiceType(activityCancelledAuditEventDocument.getServiceType());
        bpmnActivityCancelledEvent.setServiceVersion(activityCancelledAuditEventDocument.getServiceVersion());
        bpmnActivityCancelledEvent.setCause(bpmnActivityCancelledEvent.getCause());
        bpmnActivityCancelledEvent.setProcessDefinitionId(bpmnActivityCancelledEvent.getProcessDefinitionId());
        bpmnActivityCancelledEvent.setProcessInstanceId(bpmnActivityCancelledEvent.getProcessInstanceId());
        return bpmnActivityCancelledEvent;
    }
}
