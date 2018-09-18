package org.activiti.cloud.services.audit.mongo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.services.audit.api.assembler.EventResourceAssembler;
import org.activiti.cloud.services.audit.api.controllers.AuditEventsAdminController;
import org.activiti.cloud.services.audit.api.converters.APIEventToEntityConverters;
import org.activiti.cloud.services.audit.api.resources.EventResource;
import org.activiti.cloud.services.audit.api.resources.EventsRelProvider;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/v1/" + EventsRelProvider.COLLECTION_RESOURCE_REL, produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class AuditEventsAdminControllerImpl implements AuditEventsAdminController {

    private EventsRepository eventsRepository;

    private EventResourceAssembler eventResourceAssembler;

    private AlfrescoPagedResourcesAssembler<CloudRuntimeEvent> pagedResourcesAssembler;

    private final APIEventToEntityConverters eventConverters;

    @Autowired
    public AuditEventsAdminControllerImpl(EventsRepository eventsRepository,
                                          EventResourceAssembler eventResourceAssembler,
                                          APIEventToEntityConverters eventConverters,
                                          AlfrescoPagedResourcesAssembler<CloudRuntimeEvent> pagedResourcesAssembler) {
        this.eventsRepository = eventsRepository;
        this.eventResourceAssembler = eventResourceAssembler;
        this.eventConverters = eventConverters;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<EventResource> findAll(Pageable pageable) {
        Page<AuditEventDocument> allAuditInPage = eventsRepository.findAll(pageable);

        List<CloudRuntimeEvent> events = new ArrayList<>();

        for (AuditEventDocument aee : allAuditInPage.getContent()) {
            events.add(eventConverters.getConverterByEventTypeName(aee.getEventType()).convertToAPI(aee));
        }

        return pagedResourcesAssembler.toResource(pageable,
                                                  new PageImpl<CloudRuntimeEvent>(events,
                                                          pageable,
                                                          allAuditInPage.getTotalElements()),
                                                  eventResourceAssembler);
    }
}
