package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("humanEscalationDelegate")
public class HumanEscalationDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        String response = "I’m forwarding this to the front desk team so a staff member can help you directly.";

        execution.setVariable("outgoingWhatsappMessage", response);
        execution.setVariable("lastAssistantMessage", response);
    }
}