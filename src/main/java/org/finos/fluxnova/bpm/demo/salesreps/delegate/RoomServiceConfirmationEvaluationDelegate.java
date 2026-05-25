package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import org.finos.fluxnova.bpm.demo.salesreps.dto.RoomServiceConfirmationEvaluationRequest;
import org.finos.fluxnova.bpm.demo.salesreps.dto.RoomServiceConfirmationEvaluationResponse;
import org.finos.fluxnova.bpm.demo.salesreps.service.ConversationVariableMapper;
import org.finos.fluxnova.bpm.demo.salesreps.service.HotelAgentClient;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("roomServiceConfirmationEvaluationDelegate")
public class RoomServiceConfirmationEvaluationDelegate implements JavaDelegate {

    private final HotelAgentClient hotelAgentClient;
    private final ConversationVariableMapper mapper;

    public RoomServiceConfirmationEvaluationDelegate(
            HotelAgentClient hotelAgentClient,
            ConversationVariableMapper mapper
    ) {
        this.hotelAgentClient = hotelAgentClient;
        this.mapper = mapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        RoomServiceConfirmationEvaluationRequest request =
                new RoomServiceConfirmationEvaluationRequest(
                        mapper.getString(execution, "guestMessage"),
                        mapper.getMap(execution, "pendingRoomServiceOrder"),
                        mapper.getConversationHistory(execution)
                );

        RoomServiceConfirmationEvaluationResponse response =
                hotelAgentClient.evaluateRoomServiceConfirmation(request);

        if (response.updatedOrder() != null && !response.updatedOrder().isEmpty()) {
            execution.setVariable("pendingRoomServiceOrder", response.updatedOrder());
        }

        execution.setVariable(
                "roomServiceConfirmationAction",
                response.confirmationAction()
        );
        execution.setVariable("outgoingWhatsappMessage", response.message());
        execution.setVariable("lastAssistantMessage", response.message());
    }
}
