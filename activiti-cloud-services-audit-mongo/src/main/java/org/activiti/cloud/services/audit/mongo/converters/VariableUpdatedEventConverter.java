package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.model.shared.event.VariableEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudVariableUpdatedEvent;
import org.activiti.cloud.api.model.shared.impl.events.CloudVariableUpdatedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.events.VariableUpdatedEventDocument;
import org.springframework.stereotype.Component;

@Component
public class VariableUpdatedEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return VariableEvent.VariableEvents.VARIABLE_UPDATED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudVariableUpdatedEvent cloudVariableUpdatedEvent = (CloudVariableUpdatedEvent) cloudRuntimeEvent;
        VariableUpdatedEventDocument variableUpdatedEventEntity = new VariableUpdatedEventDocument(cloudVariableUpdatedEvent.getId(),
                                                                                                   cloudVariableUpdatedEvent.getTimestamp(),
                                                                                                   cloudVariableUpdatedEvent.getAppName(),
                                                                                                   cloudVariableUpdatedEvent.getAppVersion(),
                                                                                                   cloudVariableUpdatedEvent.getServiceFullName(),
                                                                                                   cloudVariableUpdatedEvent.getServiceName(),
                                                                                                   cloudVariableUpdatedEvent.getServiceType(),
                                                                                                   cloudVariableUpdatedEvent.getServiceVersion(),
                                                                                                   cloudVariableUpdatedEvent.getEntity());
        return variableUpdatedEventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        VariableUpdatedEventDocument variableUpdatedEventEntity = (VariableUpdatedEventDocument) auditEventEntity;

        CloudVariableUpdatedEventImpl variableUpdatedEvent = new CloudVariableUpdatedEventImpl(variableUpdatedEventEntity.getEventId(),
                                                                                               variableUpdatedEventEntity.getTimestamp(),
                                                                                               variableUpdatedEventEntity.getVariableInstance());
        variableUpdatedEvent.setAppName(variableUpdatedEventEntity.getAppName());
        variableUpdatedEvent.setAppVersion(variableUpdatedEventEntity.getAppVersion());
        variableUpdatedEvent.setServiceFullName(variableUpdatedEventEntity.getServiceFullName());
        variableUpdatedEvent.setServiceName(variableUpdatedEventEntity.getServiceName());
        variableUpdatedEvent.setServiceType(variableUpdatedEventEntity.getServiceType());
        variableUpdatedEvent.setServiceVersion(variableUpdatedEventEntity.getServiceVersion());

        return variableUpdatedEvent;
    }
}
