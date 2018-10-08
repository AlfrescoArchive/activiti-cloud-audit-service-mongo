package org.activiti.cloud.services.audit.mongo.streams;

import java.util.ArrayList;
import java.util.List;

import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.services.audit.api.converters.APIEventToEntityConverters;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.api.streams.AuditConsumerChannelHandler;
import org.activiti.cloud.services.audit.api.streams.AuditConsumerChannels;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.repository.EventsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(AuditConsumerChannels.class)
public class AuditConsumerChannelHandlerImpl implements AuditConsumerChannelHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(AuditConsumerChannelHandlerImpl.class);

    private final EventsRepository eventsRepository;

    private final APIEventToEntityConverters eventConverters;

    @Autowired
    public AuditConsumerChannelHandlerImpl(EventsRepository eventsRepository,
        APIEventToEntityConverters eventConverters) {
        this.eventsRepository = eventsRepository;
        this.eventConverters = eventConverters;
    }

    @Override
    @StreamListener(AuditConsumerChannels.AUDIT_CONSUMER)
    public void receiveCloudRuntimeEvent(CloudRuntimeEvent<?, ?>... events) {
        List<AuditEventDocument> incomingEvents = new ArrayList();
        if (events != null) {
            for (CloudRuntimeEvent event : events) {
                EventToEntityConverter converter = eventConverters.getConverterByEventTypeName(event.getEventType().name());
                if (converter != null) {
                    incomingEvents.add((AuditEventDocument) converter.convertToEntity(event));
                } else {
                    LOGGER.warn(">>> Ignoring CloudRuntimeEvents type: " + event.getEventType().name());
                }
            }
        }
        eventsRepository.saveAll(incomingEvents);
    }

}