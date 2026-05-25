package org.finos.fluxnova.bpm.demo.salesreps.dto;

import java.util.List;
import java.util.Map;

public record RoomServiceConfirmationEvaluationRequest(
        String guestMessage,
        Map<String, Object> pendingOrder,
        List<ConversationMessageDto> conversationHistory
) {}
