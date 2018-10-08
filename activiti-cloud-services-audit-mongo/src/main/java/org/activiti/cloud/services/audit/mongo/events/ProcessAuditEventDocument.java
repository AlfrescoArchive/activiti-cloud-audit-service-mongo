package org.activiti.cloud.services.audit.mongo.events;

import org.activiti.api.process.model.ProcessInstance;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class ProcessAuditEventDocument extends AuditEventDocument {

    private ProcessInstance processInstance;

    private String nestedProcessDefinitionId;
    private String nestedProcessInstanceId;

    public ProcessAuditEventDocument() {
    }

    public ProcessAuditEventDocument(String eventId,
                                     Long timestamp,
                                     String eventType) {
        super(eventId,
              timestamp,
              eventType);
    }

    public ProcessAuditEventDocument(String eventId,
                                     Long timestamp,
                                     String eventType,
                                     String appName,
                                     String appVersion,
                                     String serviceName,
                                     String serviceFullName,
                                     String serviceType,
                                     String serviceVersion,
                                     ProcessInstance processInstance,
                                     String nestedProcessDefinitionId,
                                     String nestedProcessInstanceId) {
        super(eventId,
              timestamp,
              eventType);

        setAppName(appName);
        setAppVersion(appVersion);
        setServiceName(serviceName);
        setServiceFullName(serviceFullName);
        setServiceType(serviceType);
        setServiceVersion(serviceVersion);
        setProcessDefinitionId((processInstance != null) ? processInstance.getProcessDefinitionId() : null);
        setProcessInstanceId((processInstance != null) ? processInstance.getId() : null);

        this.processInstance = processInstance;
        this.nestedProcessDefinitionId = nestedProcessDefinitionId;
        this.nestedProcessInstanceId = nestedProcessInstanceId;
        setEntityId(processInstance.getId());
    }

    public ProcessAuditEventDocument(String eventId,
                                     Long timestamp,
                                     String eventType,
                                     String appName,
                                     String appVersion,
                                     String serviceName,
                                     String serviceFullName,
                                     String serviceType,
                                     String serviceVersion,
                                     ProcessInstance processInstance) {
        super(eventId,
              timestamp,
              eventType);
        setAppName(appName);
        setAppVersion(appVersion);
        setServiceName(serviceName);
        setServiceFullName(serviceFullName);
        setServiceType(serviceType);
        setServiceVersion(serviceVersion);
        setProcessDefinitionId((processInstance != null) ? processInstance.getProcessDefinitionId() : null);
        setProcessInstanceId((processInstance != null) ? processInstance.getId() : null);

        this.processInstance = processInstance;
        setEntityId(processInstance.getId());
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public String getNestedProcessDefinitionId() {
        return nestedProcessDefinitionId;
    }

    public String getNestedProcessInstanceId() {
        return nestedProcessInstanceId;
    }

    public void setNestedProcessDefinitionId(String nestedProcessDefinitionId) {
        this.nestedProcessDefinitionId = nestedProcessDefinitionId;
    }

    public void setNestedProcessInstanceId(String nestedProcessInstanceId) {
        this.nestedProcessInstanceId = nestedProcessInstanceId;
    }
}