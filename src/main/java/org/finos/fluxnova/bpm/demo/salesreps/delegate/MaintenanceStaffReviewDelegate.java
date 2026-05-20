package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import org.finos.fluxnova.bpm.demo.salesreps.service.ConversationVariableMapper;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("maintenanceStaffReviewDelegate")
public class MaintenanceStaffReviewDelegate implements JavaDelegate {

    private final ConversationVariableMapper mapper;

    public MaintenanceStaffReviewDelegate(ConversationVariableMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String roomNumber = mapper.getString(execution, "roomNumber");

        String response = "Maintenance has been notified"
                + (roomNumber != null ? " for room " + roomNumber : "")
                + ". A staff member will follow up shortly.";

        execution.setVariable("outgoingWhatsappMessage", response);
        execution.setVariable("lastAssistantMessage", response);
    }
}