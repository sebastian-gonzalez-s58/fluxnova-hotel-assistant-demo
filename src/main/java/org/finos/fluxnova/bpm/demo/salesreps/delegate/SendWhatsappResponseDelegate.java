package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.finos.fluxnova.bpm.demo.salesreps.dto.ConversationMessageDto;
import org.finos.fluxnova.bpm.demo.salesreps.service.ConversationVariableMapper;
import org.finos.fluxnova.bpm.demo.salesreps.service.WhatsappMessageStore;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("sendWhatsappResponseDelegate")
public class SendWhatsappResponseDelegate implements JavaDelegate {

    private final ConversationVariableMapper mapper;
    private final ObjectMapper objectMapper;
    private final WhatsappMessageStore messageStore;

    public SendWhatsappResponseDelegate(
            ConversationVariableMapper mapper,
            ObjectMapper objectMapper,
            WhatsappMessageStore messageStore
    ) {
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        this.messageStore = messageStore;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String message = mapper.getString(
                execution,
                "outgoingWhatsappMessage"
        );

        String processInstanceId = execution.getProcessInstanceId();

        messageStore.save(processInstanceId, message);

        System.out.println("Sending WhatsApp message");
        System.out.println("To: +5215512345678");
        System.out.println("Message: " + message);

        List<ConversationMessageDto> history =
                mapper.getConversationHistory(execution);

        if (message != null && !message.isBlank()) {
            history.add(
                    new ConversationMessageDto(
                            "assistant",
                            message
                    )
            );
        }

        execution.setVariable(
                "conversationHistoryJson",
                objectMapper.writeValueAsString(history)
        );
    }
}