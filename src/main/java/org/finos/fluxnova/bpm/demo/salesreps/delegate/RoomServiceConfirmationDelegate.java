package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import org.finos.fluxnova.bpm.demo.salesreps.dto.RoomServiceConfirmationRequest;
import org.finos.fluxnova.bpm.demo.salesreps.dto.RoomServiceConfirmationResponse;
import org.finos.fluxnova.bpm.demo.salesreps.service.ConversationVariableMapper;
import org.finos.fluxnova.bpm.demo.salesreps.service.HotelAgentClient;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("roomServiceConfirmationDelegate")
public class RoomServiceConfirmationDelegate implements JavaDelegate {

    private final HotelAgentClient hotelAgentClient;
    private final ConversationVariableMapper mapper;

    public RoomServiceConfirmationDelegate(
            HotelAgentClient hotelAgentClient,
            ConversationVariableMapper mapper
    ) {
        this.hotelAgentClient = hotelAgentClient;
        this.mapper = mapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        RoomServiceConfirmationRequest request = new RoomServiceConfirmationRequest(
                mapper.getExtraction(execution),
                mapper.getConversationHistory(execution)
        );

        RoomServiceConfirmationResponse response =
                hotelAgentClient.generateRoomServiceConfirmation(request);

        execution.setVariable("pendingRoomServiceOrder", response.pendingOrder());
        execution.setVariable("outgoingWhatsappMessage", response.message());
        execution.setVariable("lastAssistantMessage", response.message());
    }
}
