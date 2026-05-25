package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import org.finos.fluxnova.bpm.demo.salesreps.service.ConversationVariableMapper;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("roomServiceDelegate")
public class RoomServiceDelegate implements JavaDelegate {

    private final ConversationVariableMapper mapper;

    public RoomServiceDelegate(ConversationVariableMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        Map<String, Object> order = mapper.getMap(execution, "pendingRoomServiceOrder");

        String roomNumber = mapper.getString(execution, "roomNumber");
        if (roomNumber == null && order.get("roomNumber") != null) {
            roomNumber = order.get("roomNumber").toString();
        }

        String response = "Thanks, your room service order has been placed"
                + (roomNumber != null ? " for room " + roomNumber : "")
                + ". The hotel team will prepare it shortly.";

        execution.setVariable("roomServiceOrder", order);
        execution.setVariable("outgoingWhatsappMessage", response);
        execution.setVariable("lastAssistantMessage", response);
    }
}
