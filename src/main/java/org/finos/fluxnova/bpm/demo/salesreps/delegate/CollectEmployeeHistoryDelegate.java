package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.finos.fluxnova.bpm.engine.history.HistoricVariableInstance;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component("collectEmployeeHistoryDelegate")
public class CollectEmployeeHistoryDelegate implements JavaDelegate {

    private final HistoryService historyService;
    private final ObjectMapper objectMapper;

    public CollectEmployeeHistoryDelegate(HistoryService historyService, ObjectMapper objectMapper) {
        this.historyService = historyService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        List<HistoricVariableInstance> ratingVars = historyService
                .createHistoricVariableInstanceQuery()
                .variableName("ratingJson")
                .list();

        List<HistoricVariableInstance> commentVars = historyService
                .createHistoricVariableInstanceQuery()
                .variableName("educationalComment")
                .list();

        List<Map<String, Object>> evaluations = new ArrayList<>();

        int maxSize = Math.max(ratingVars.size(), commentVars.size());

        for (int i = 0; i < maxSize; i++) {
            Map<String, Object> evaluation = new LinkedHashMap<>();

            if (i < ratingVars.size()) {
                Object ratingValue = ratingVars.get(i).getValue();
                evaluation.put("ratingJson", parseJsonIfPossible(ratingValue));
            } else {
                evaluation.put("ratingJson", null);
            }

            if (i < commentVars.size()) {
                Object commentValue = commentVars.get(i).getValue();
                evaluation.put("educationalComment", commentValue != null ? commentValue.toString() : null);
            } else {
                evaluation.put("educationalComment", null);
            }

            evaluations.add(evaluation);
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("evaluations", evaluations);

        String payloadJson = objectMapper.writeValueAsString(payload);

        System.out.println("Collected employee history payload: " + payloadJson);

        execution.setVariable("employeeEvaluationsPayload", payloadJson);
        execution.setVariable("employeeEvaluationCount", evaluations.size());
    }

    private Object parseJsonIfPossible(Object value) {
        if (value == null) {
            return null;
        }

        if (!(value instanceof String stringValue)) {
            return value;
        }

        try {
            return objectMapper.readValue(stringValue, Object.class);
        } catch (JsonProcessingException e) {
            return stringValue;
        }
    }
}