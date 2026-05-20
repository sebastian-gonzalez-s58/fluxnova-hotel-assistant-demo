package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import org.finos.fluxnova.bpm.demo.salesreps.dto.ConversationMessageDto;
import org.finos.fluxnova.bpm.demo.salesreps.service.ConversationVariableMapper;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("appendConversationDelegate")
public class AppendConversationDelegate implements JavaDelegate {

    private final ObjectMapper objectMapper;
    private final ConversationVariableMapper mapper;

    public AppendConversationDelegate(
            ObjectMapper objectMapper,
            ConversationVariableMapper mapper
    ) {
        this.objectMapper = objectMapper;
        this.mapper = mapper;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<ConversationMessageDto> history =
                mapper.getConversationHistory(execution);

        String guestMessage =
                mapper.getString(execution, "guestMessage");

        if (guestMessage != null && !guestMessage.isBlank()) {
            history.add(
                    new ConversationMessageDto(
                            "guest",
                            guestMessage
                    )
            );
        }

        execution.setVariable(
                "conversationHistoryJson",
                objectMapper.writeValueAsString(history)
        );
    }
}