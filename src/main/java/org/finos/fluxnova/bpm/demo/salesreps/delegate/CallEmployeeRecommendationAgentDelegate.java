package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component("callEmployeeRecommendationAgentDelegate")
public class CallEmployeeRecommendationAgentDelegate implements JavaDelegate {

    private final ObjectMapper objectMapper;

    public CallEmployeeRecommendationAgentDelegate(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String payloadJson = (String) execution.getVariable("employeeEvaluationsPayload");

        if (payloadJson == null || payloadJson.isBlank()) {
            throw new IllegalArgumentException("employeeEvaluationsPayload is null or blank");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(payloadJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8000/employee-review",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        String recommendationJson = response.getBody();

        if (recommendationJson == null || recommendationJson.isBlank()) {
            throw new IllegalStateException("Agent returned an empty response");
        }

        System.out.println("Employee recommendation JSON: " + recommendationJson);

        execution.setVariable("employeeRecommendationJson", recommendationJson);

        Map<String, Object> parsed = objectMapper.readValue(recommendationJson, Map.class);
        execution.setVariable("employeeRecommendation", parsed.get("recommendation"));
        execution.setVariable("employeeRecommendationSummary", parsed.get("summary"));
        execution.setVariable("employeeManagerNote", parsed.get("manager_note"));
    }
}