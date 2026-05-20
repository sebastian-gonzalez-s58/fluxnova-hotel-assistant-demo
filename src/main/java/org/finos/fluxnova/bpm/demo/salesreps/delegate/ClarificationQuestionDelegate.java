package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import org.finos.fluxnova.bpm.demo.salesreps.dto.ClarificationRequest;
import org.finos.fluxnova.bpm.demo.salesreps.dto.ClarificationResponse;
import org.finos.fluxnova.bpm.demo.salesreps.service.ConversationVariableMapper;
import org.finos.fluxnova.bpm.demo.salesreps.service.HotelAgentClient;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("clarificationQuestionDelegate")
public class ClarificationQuestionDelegate implements JavaDelegate {

    private final HotelAgentClient hotelAgentClient;
    private final ConversationVariableMapper mapper;

    public ClarificationQuestionDelegate(
            HotelAgentClient hotelAgentClient,
            ConversationVariableMapper mapper
    ) {
        this.hotelAgentClient = hotelAgentClient;
        this.mapper = mapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        ClarificationRequest request = new ClarificationRequest(
                mapper.getExtraction(execution),
                mapper.getConversationHistory(execution)
        );

        ClarificationResponse response = hotelAgentClient.generateClarification(request);

        execution.setVariable("outgoingWhatsappMessage", response.message());
        execution.setVariable("lastAssistantMessage", response.message());
    }
}