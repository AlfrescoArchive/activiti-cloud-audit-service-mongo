/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.services.audit.mongo.channel;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.activiti.api.process.model.events.ProcessRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.services.audit.api.converters.APIEventToEntityConverters;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.repository.EventsRepository;
import org.activiti.cloud.services.audit.mongo.streams.AuditConsumerChannelHandlerImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class AuditConsumerChannelHandlerTest {

    @InjectMocks
    private AuditConsumerChannelHandlerImpl handler;

    @Mock
    private APIEventToEntityConverters converters;

    @Mock
    private EventsRepository eventsRepository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void receiveShouldStoreAllReceivedEvents() throws Exception {
        //given
        CloudRuntimeEvent cloudRuntimeEvent = mock(CloudRuntimeEvent.class);
        when(cloudRuntimeEvent.getEventType()).thenReturn(ProcessRuntimeEvent.ProcessEvents.PROCESS_CREATED);
        EventToEntityConverter converter = mock(EventToEntityConverter.class);
        when(converters.getConverterByEventTypeName(ProcessRuntimeEvent.ProcessEvents.PROCESS_CREATED.name())).thenReturn(converter);
        AuditEventDocument entity = mock(AuditEventDocument.class);
        when(converter.convertToEntity(cloudRuntimeEvent)).thenReturn(entity);

        CloudRuntimeEvent[] events = {cloudRuntimeEvent};
        List<AuditEventDocument> incomingEvents = new ArrayList();
        incomingEvents.add(entity);

        //when
        handler.receiveCloudRuntimeEvent(events);

        //then
        verify(eventsRepository).saveAll(incomingEvents);
    }
}