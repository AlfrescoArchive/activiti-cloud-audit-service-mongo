package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.model.shared.event.VariableEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudVariableDeletedEvent;
import org.activiti.cloud.api.model.shared.impl.events.CloudVariableDeletedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.events.VariableDeletedEventDocument;
import org.springframework.stereotype.Component;

@Component
public class VariableDeletedEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return VariableEvent.VariableEvents.VARIABLE_DELETED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudVariableDeletedEvent cloudVariableDeletedEvent = (CloudVariableDeletedEvent) cloudRuntimeEvent;
        VariableDeletedEventDocument variableDeletedEventEntity = new VariableDeletedEventDocument(cloudVariableDeletedEvent.getId(),
                                                                                                   cloudVariableDeletedEvent.getTimestamp(),
                                                                                                   cloudVariableDeletedEvent.getAppName(),
                                                                                                   cloudVariableDeletedEvent.getAppVersion(),
                                                                                                   cloudVariableDeletedEvent.getServiceFullName(),
                                                                                                   cloudVariableDeletedEvent.getServiceName(),
                                                                                                   cloudVariableDeletedEvent.getServiceType(),
                                                                                                   cloudVariableDeletedEvent.getServiceVersion(),
                                                                                                   cloudVariableDeletedEvent.getEntity());
        return variableDeletedEventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        VariableDeletedEventDocument variableDeletedEventEntity = (VariableDeletedEventDocument) auditEventEntity;

        CloudVariableDeletedEventImpl variableDeletedEvent = new CloudVariableDeletedEventImpl(variableDeletedEventEntity.getEventId(),
                                                                                               variableDeletedEventEntity.getTimestamp(),
                                                                                               variableDeletedEventEntity.getVariableInstance());
        variableDeletedEvent.setAppName(variableDeletedEventEntity.getAppName());
        variableDeletedEvent.setAppVersion(variableDeletedEventEntity.getAppVersion());
        variableDeletedEvent.setServiceFullName(variableDeletedEventEntity.getServiceFullName());
        variableDeletedEvent.setServiceName(variableDeletedEventEntity.getServiceName());
        variableDeletedEvent.setServiceType(variableDeletedEventEntity.getServiceType());
        variableDeletedEvent.setServiceVersion(variableDeletedEventEntity.getServiceVersion());

        return variableDeletedEvent;
    }
}
