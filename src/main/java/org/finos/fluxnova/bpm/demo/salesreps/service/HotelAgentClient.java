package org.finos.fluxnova.bpm.demo.salesreps.service;

import org.finos.fluxnova.bpm.demo.salesreps.dto.ClarificationRequest;
import org.finos.fluxnova.bpm.demo.salesreps.dto.ClarificationResponse;
import org.finos.fluxnova.bpm.demo.salesreps.dto.FaqResponse;
import org.finos.fluxnova.bpm.demo.salesreps.dto.HotelConversationRequest;
import org.finos.fluxnova.bpm.demo.salesreps.dto.HotelExtractionResponse;
import org.finos.fluxnova.bpm.demo.salesreps.dto.RoomServiceConfirmationEvaluationRequest;
import org.finos.fluxnova.bpm.demo.salesreps.dto.RoomServiceConfirmationEvaluationResponse;
import org.finos.fluxnova.bpm.demo.salesreps.dto.RoomServiceConfirmationRequest;
import org.finos.fluxnova.bpm.demo.salesreps.dto.RoomServiceConfirmationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HotelAgentClient {

    private final RestClient hotelAgentRestClient;

    public HotelAgentClient(RestClient hotelAgentRestClient) {
        this.hotelAgentRestClient = hotelAgentRestClient;
    }

    public HotelExtractionResponse extractIntent(
            HotelConversationRequest request
    ) {
        return hotelAgentRestClient.post()
                .uri("/hotel/extract-intent")
                .body(request)
                .retrieve()
                .body(HotelExtractionResponse.class);
    }

    public ClarificationResponse generateClarification(
            ClarificationRequest request
    ) {
        return hotelAgentRestClient.post()
                .uri("/hotel/generate-clarification")
                .body(request)
                .retrieve()
                .body(ClarificationResponse.class);
    }

    public FaqResponse generateFaqResponse(
            HotelConversationRequest request
    ) {
        return hotelAgentRestClient.post()
                .uri("/hotel/faq-response")
                .body(request)
                .retrieve()
                .body(FaqResponse.class);
    }

    public RoomServiceConfirmationResponse generateRoomServiceConfirmation(
            RoomServiceConfirmationRequest request
    ) {
        return hotelAgentRestClient.post()
                .uri("/hotel/room-service-confirmation")
                .body(request)
                .retrieve()
                .body(RoomServiceConfirmationResponse.class);
    }

    public RoomServiceConfirmationEvaluationResponse evaluateRoomServiceConfirmation(
            RoomServiceConfirmationEvaluationRequest request
    ) {
        return hotelAgentRestClient.post()
                .uri("/hotel/evaluate-room-service-confirmation")
                .body(request)
                .retrieve()
                .body(RoomServiceConfirmationEvaluationResponse.class);
    }
}
