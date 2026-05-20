package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import org.finos.fluxnova.bpm.demo.salesreps.dto.FaqResponse;
import org.finos.fluxnova.bpm.demo.salesreps.dto.HotelConversationRequest;
import org.finos.fluxnova.bpm.demo.salesreps.service.ConversationVariableMapper;
import org.finos.fluxnova.bpm.demo.salesreps.service.HotelAgentClient;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("faqDelegate")
public class FaqDelegate implements JavaDelegate {

    private final HotelAgentClient hotelAgentClient;
    private final ConversationVariableMapper mapper;

    public FaqDelegate(
            HotelAgentClient hotelAgentClient,
            ConversationVariableMapper mapper
    ) {
        this.hotelAgentClient = hotelAgentClient;
        this.mapper = mapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        HotelConversationRequest request = new HotelConversationRequest(
                execution.getProcessInstanceId(),
                mapper.getString(execution, "guestMessage"),
                mapper.getConversationHistory(execution),
                mapper.getKnownContext(execution)
        );

        FaqResponse response = hotelAgentClient.generateFaqResponse(request);

        execution.setVariable("outgoingWhatsappMessage", response.message());
        execution.setVariable("lastAssistantMessage", response.message());
    }
}