package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import org.springframework.stereotype.Component;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

@Component("prepareCommunicationInputDelegate")
public class PrepareCommunicationInputDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {

        String channel = (String) execution.getVariable("channel");
        String transcript = (String) execution.getVariable("transcript");

        if (channel == null || channel.isBlank()) {
            channel = "phone_call";
        }

        if (!"email".equals(channel) && !"phone_call".equals(channel)) {
            throw new IllegalArgumentException("channel must be either 'email' or 'phone_call'");
        }

        if (transcript == null || transcript.isBlank()) {
            throw new IllegalArgumentException("transcript must not be null or blank");
        }

        String evaluationInput = """
                Communication channel: %s

                Transcript:
                %s
                """.formatted(channel, transcript);

        execution.setVariable("channel", channel);
        execution.setVariable("evaluationInput", evaluationInput);
    }
}