package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.process.model.events.BPMNActivityEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudBPMNActivityStartedEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNActivityStartedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.ActivityStartedEventDocument;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.springframework.stereotype.Component;

@Component
public class ActivityStartedEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return BPMNActivityEvent.ActivityEvents.ACTIVITY_STARTED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudBPMNActivityStartedEvent cloudActivityStartedEvent = (CloudBPMNActivityStartedEvent) cloudRuntimeEvent;
        ActivityStartedEventDocument activityStartedAuditEventDocument = new ActivityStartedEventDocument(cloudActivityStartedEvent.getId(),
                                                                                                          cloudActivityStartedEvent.getTimestamp(),
                                                                                                          cloudActivityStartedEvent.getAppName(),
                                                                                                          cloudActivityStartedEvent.getAppVersion(),
                                                                                                          cloudActivityStartedEvent.getServiceFullName(),
                                                                                                          cloudActivityStartedEvent.getServiceName(),
                                                                                                          cloudActivityStartedEvent.getServiceType(),
                                                                                                          cloudActivityStartedEvent.getServiceVersion(),
                                                                                                          cloudActivityStartedEvent.getEntity());
        activityStartedAuditEventDocument.setEntityId(cloudActivityStartedEvent.getProcessInstanceId());
        activityStartedAuditEventDocument.setProcessDefinitionId(cloudActivityStartedEvent.getProcessDefinitionId());
        activityStartedAuditEventDocument.setProcessInstanceId(cloudActivityStartedEvent.getProcessInstanceId());
        return activityStartedAuditEventDocument;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        ActivityStartedEventDocument activityStartedAuditEventDocument = (ActivityStartedEventDocument) auditEventEntity;

        CloudBPMNActivityStartedEventImpl bpmnActivityStartedEvent = new CloudBPMNActivityStartedEventImpl(activityStartedAuditEventDocument.getEventId(),
                                                                                                           activityStartedAuditEventDocument.getTimestamp(),
                                                                                                           activityStartedAuditEventDocument.getBpmnActivity(),
                                                                                                           activityStartedAuditEventDocument.getProcessDefinitionId(),
                                                                                                           activityStartedAuditEventDocument.getProcessInstanceId());
        bpmnActivityStartedEvent.setAppName(activityStartedAuditEventDocument.getAppName());
        bpmnActivityStartedEvent.setAppVersion(activityStartedAuditEventDocument.getAppVersion());
        bpmnActivityStartedEvent.setServiceFullName(activityStartedAuditEventDocument.getServiceFullName());
        bpmnActivityStartedEvent.setServiceName(activityStartedAuditEventDocument.getServiceName());
        bpmnActivityStartedEvent.setServiceType(activityStartedAuditEventDocument.getServiceType());
        bpmnActivityStartedEvent.setServiceVersion(activityStartedAuditEventDocument.getServiceVersion());
        bpmnActivityStartedEvent.setProcessDefinitionId(activityStartedAuditEventDocument.getProcessDefinitionId());
        bpmnActivityStartedEvent.setProcessInstanceId(activityStartedAuditEventDocument.getProcessInstanceId());
        return bpmnActivityStartedEvent;
    }
}
