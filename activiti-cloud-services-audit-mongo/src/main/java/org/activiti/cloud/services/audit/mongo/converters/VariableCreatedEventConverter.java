package org.activiti.cloud.services.audit.mongo.converters;

import org.activiti.api.model.shared.event.VariableEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudVariableCreatedEvent;
import org.activiti.cloud.api.model.shared.impl.events.CloudVariableCreatedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.events.VariableCreatedEventDocument;
import org.springframework.stereotype.Component;

@Component
public class VariableCreatedEventConverter implements EventToEntityConverter<AuditEventDocument> {

    @Override
    public String getSupportedEvent() {
        return VariableEvent.VariableEvents.VARIABLE_CREATED.name();
    }

    @Override
    public AuditEventDocument convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudVariableCreatedEvent cloudVariableCreatedEvent = (CloudVariableCreatedEvent) cloudRuntimeEvent;
        VariableCreatedEventDocument variableCreatedEventEntity = new VariableCreatedEventDocument(cloudVariableCreatedEvent.getId(),
                                                                                                   cloudVariableCreatedEvent.getTimestamp(),
                                                                                                   cloudVariableCreatedEvent.getAppName(),
                                                                                                   cloudVariableCreatedEvent.getAppVersion(),
                                                                                                   cloudVariableCreatedEvent.getServiceFullName(),
                                                                                                   cloudVariableCreatedEvent.getServiceName(),
                                                                                                   cloudVariableCreatedEvent.getServiceType(),
                                                                                                   cloudVariableCreatedEvent.getServiceVersion(),
                                                                                                   cloudVariableCreatedEvent.getEntity());
        return variableCreatedEventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventDocument auditEventEntity) {
        VariableCreatedEventDocument variableCreatedEventEntity = (VariableCreatedEventDocument) auditEventEntity;

        CloudVariableCreatedEventImpl cloudVariableCreatedEvent = new CloudVariableCreatedEventImpl(variableCreatedEventEntity.getEventId(),
                                                                                                    variableCreatedEventEntity.getTimestamp(),
                                                                                                    variableCreatedEventEntity.getVariableInstance());
        cloudVariableCreatedEvent.setAppName(variableCreatedEventEntity.getAppName());
        cloudVariableCreatedEvent.setAppVersion(variableCreatedEventEntity.getAppVersion());
        cloudVariableCreatedEvent.setServiceFullName(variableCreatedEventEntity.getServiceFullName());
        cloudVariableCreatedEvent.setServiceName(variableCreatedEventEntity.getServiceName());
        cloudVariableCreatedEvent.setServiceType(variableCreatedEventEntity.getServiceType());
        cloudVariableCreatedEvent.setServiceVersion(variableCreatedEventEntity.getServiceVersion());

        return cloudVariableCreatedEvent;
    }
}
