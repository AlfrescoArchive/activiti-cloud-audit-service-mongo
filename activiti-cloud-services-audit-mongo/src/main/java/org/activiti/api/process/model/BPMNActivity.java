package org.activiti.api.process.model;

import org.activiti.api.process.model.BPMNElement;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public interface BPMNActivity extends BPMNElement {

    String getActivityName();

    String getActivityType();

    String getElementId();
}