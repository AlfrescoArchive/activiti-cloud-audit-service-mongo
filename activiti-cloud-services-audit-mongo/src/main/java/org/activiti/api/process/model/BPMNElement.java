package org.activiti.api.process.model;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
@QueryEmbeddable
public interface BPMNElement {

    String getProcessInstanceId();

    String getProcessDefinitionId();
}