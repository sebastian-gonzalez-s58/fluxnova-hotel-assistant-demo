package org.finos.fluxnova.bpm.demo.salesreps.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.finos.fluxnova.bpm.demo.salesreps.dto.ConversationMessageDto;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConversationVariableMapper {

    private final ObjectMapper objectMapper;

    public ConversationVariableMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getString(DelegateExecution execution, String variableName) {
        Object value = execution.getVariable(variableName);
        return value == null ? null : value.toString();
    }

    public List<ConversationMessageDto> getConversationHistory(
            DelegateExecution execution
    ) {
        try {
            String raw = (String) execution.getVariable("conversationHistoryJson");

            if (raw == null || raw.isBlank()) {
                return new ArrayList<>();
            }

            return objectMapper.readValue(
                    raw,
                    new TypeReference<List<ConversationMessageDto>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> getKnownContext(
            DelegateExecution execution
    ) {
        Object raw = execution.getVariable("knownContext");

        if (raw == null) {
            return new HashMap<>();
        }

        return objectMapper.convertValue(
                raw,
                new TypeReference<Map<String, Object>>() {}
        );
    }

    public Map<String, Object> getExtraction(
            DelegateExecution execution
    ) {
        Object raw = execution.getVariable("extraction");

        if (raw == null) {
            return new HashMap<>();
        }

        return objectMapper.convertValue(
                raw,
                new TypeReference<Map<String, Object>>() {}
        );
    }
}