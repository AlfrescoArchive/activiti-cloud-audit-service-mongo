package org.activiti.cloud.services.audit.mongo.events;

import org.activiti.api.process.model.BPMNActivity;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryInit;

@QueryEntity
public abstract class BPMNActivityAuditEventDocument extends AuditEventDocument {

    @QueryInit("*.*")
    private BPMNActivity bpmnActivity;

    public BPMNActivityAuditEventDocument() {
    }

    public BPMNActivityAuditEventDocument(String eventId,
                                          Long timestamp,
                                          String eventType) {
        super(eventId,
              timestamp,
              eventType);
    }

    public BPMNActivityAuditEventDocument(String eventId,
                                          Long timestamp,
                                          String eventType,
                                          String appName,
                                          String appVersion,
                                          String serviceName,
                                          String serviceFullName,
                                          String serviceType,
                                          String serviceVersion,
                                          BPMNActivity bpmnActivity) {
        super(eventId,
              timestamp,
              eventType);
        setAppName(appName);
        setAppVersion(appVersion);
        setServiceName(serviceName);
        setServiceFullName(serviceFullName);
        setServiceType(serviceType);
        setServiceVersion(serviceVersion);
        this.bpmnActivity = bpmnActivity;
        setEntityId(bpmnActivity.getProcessInstanceId());
    }

    public BPMNActivity getBpmnActivity() {
        return bpmnActivity;
    }

    public void setBpmnActivity(BPMNActivity bpmnActivity) {
        this.bpmnActivity = bpmnActivity;
    }
}
