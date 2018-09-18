package org.activiti.cloud.services.audit.mongo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.services.audit.api.assembler.EventResourceAssembler;
import org.activiti.cloud.services.audit.api.controllers.AuditEventsController;
import org.activiti.cloud.services.audit.api.converters.APIEventToEntityConverters;
import org.activiti.cloud.services.audit.api.resources.EventResource;
import org.activiti.cloud.services.audit.api.resources.EventsRelProvider;
import org.activiti.cloud.services.audit.mongo.SecurityPoliciesApplicationServiceImpl;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.repository.EventQueryDslBuilder;
import org.activiti.cloud.services.audit.mongo.repository.EventsRepository;
import org.activiti.cloud.services.audit.mongo.repository.SearchOperation;
import org.activiti.spring.security.policies.SecurityPolicyAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Joiner;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping(value = "/v1/" + EventsRelProvider.COLLECTION_RESOURCE_REL, produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class AuditEventsControllerImpl implements AuditEventsController {

    private final EventsRepository eventsRepository;

    private EventResourceAssembler eventResourceAssembler;

    private AlfrescoPagedResourcesAssembler<CloudRuntimeEvent> pagedResourcesAssembler;

    private SecurityPoliciesApplicationServiceImpl securityPoliciesApplicationService;

    private final APIEventToEntityConverters eventConverters;

    @Autowired
    public AuditEventsControllerImpl(EventsRepository eventsRepository,
                                     EventResourceAssembler eventResourceAssembler,
                                     APIEventToEntityConverters eventConverters,
                                     AlfrescoPagedResourcesAssembler<CloudRuntimeEvent> pagedResourcesAssembler,
                                     SecurityPoliciesApplicationServiceImpl securityPoliciesApplicationService) {
        this.eventsRepository = eventsRepository;
        this.eventConverters = eventConverters;
        this.eventResourceAssembler = eventResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.securityPoliciesApplicationService = securityPoliciesApplicationService;
    }

    @Override
    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
    public EventResource findById(@PathVariable String eventId) {
        Optional<AuditEventDocument> findResult = eventsRepository.findByEventId(eventId);
        if (!findResult.isPresent()) {
            throw new RuntimeException("Unable to find event for the given id:'" + eventId + "'");
        }
        AuditEventDocument processEngineEventEntity = findResult.get();

        if (!securityPoliciesApplicationService.canRead(processEngineEventEntity.getProcessDefinitionId(),processEngineEventEntity.getServiceName())){
            throw new RuntimeException("Operation not permitted for " + processEngineEventEntity.getProcessDefinitionId());
        }

        CloudRuntimeEvent cloudRuntimeEvent = eventConverters.getConverterByEventTypeName(processEngineEventEntity.getEventType()).convertToAPI(processEngineEventEntity);
        return eventResourceAssembler.toResource(cloudRuntimeEvent);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<EventResource> findAll(@RequestParam(value = "search",
    required = false) String search, Pageable pageable) {
        Predicate expression = createSearchSpec(search);
        Predicate predicate = securityPoliciesApplicationService.createSearchSpecWithSecurity(expression,
                                                                                              SecurityPolicyAccess.READ);
        Page<AuditEventDocument> allAuditInPage = null;
        if (predicate == null) {
            allAuditInPage = eventsRepository.findAll(pageable);
        } else {
            allAuditInPage = eventsRepository.findAll(predicate, pageable);
        }

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

    private Predicate createSearchSpec(String search) {
        EventQueryDslBuilder builder = new EventQueryDslBuilder();

        if (search != null && !search.isEmpty()) {
            String operationSetExper = Joiner.on("|")
                    .join(SearchOperation.SIMPLE_OPERATION_SET);
            Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)([a-zA-Z0-9-_]+?)(\\p{Punct}?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1),
                             matcher.group(2),
                             matcher.group(4),
                             matcher.group(3),
                             matcher.group(5));
            }
        }
        return builder.build();
    }
}
