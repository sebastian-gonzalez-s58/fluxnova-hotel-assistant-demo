package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.finos.fluxnova.bpm.demo.salesreps.dto.HotelConversationRequest;
import org.finos.fluxnova.bpm.demo.salesreps.dto.HotelExtractionResponse;
import org.finos.fluxnova.bpm.demo.salesreps.service.ConversationVariableMapper;
import org.finos.fluxnova.bpm.demo.salesreps.service.HotelAgentClient;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("extractIntentDelegate")
public class ExtractIntentDelegate implements JavaDelegate {

    private final HotelAgentClient hotelAgentClient;
    private final ConversationVariableMapper mapper;
    private final ObjectMapper objectMapper;

    public ExtractIntentDelegate(
            HotelAgentClient hotelAgentClient,
            ConversationVariableMapper mapper,
            ObjectMapper objectMapper
    ) {
        this.hotelAgentClient = hotelAgentClient;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String conversationId = execution.getProcessInstanceId();
        String guestMessage = mapper.getString(execution, "guestMessage");

        HotelConversationRequest request = new HotelConversationRequest(
                conversationId,
                guestMessage,
                mapper.getConversationHistory(execution),
                mapper.getKnownContext(execution)
        );

        HotelExtractionResponse response = hotelAgentClient.extractIntent(request);

        Map<String, Object> extraction = objectMapper.convertValue(response, Map.class);

        execution.setVariable("extraction", extraction);
        execution.setVariable("intent", response.intent());
        execution.setVariable("confidence", response.confidence());
        execution.setVariable("containsEmergency", response.containsEmergency());
        execution.setVariable("roomNumber", response.roomNumber());
        execution.setVariable("language", response.language());
        execution.setVariable("missingFields", response.missingFields());
        execution.setVariable("requestComplete", response.requestComplete());
        execution.setVariable("extractedEntities", response.extractedEntities());
    }
}