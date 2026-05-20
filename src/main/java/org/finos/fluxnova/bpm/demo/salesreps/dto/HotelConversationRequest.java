package org.finos.fluxnova.bpm.demo.salesreps.dto;

import java.util.List;
import java.util.Map;

public record HotelConversationRequest(
        String conversationId,
        String guestMessage,
        List<ConversationMessageDto> conversationHistory,
        Map<String, Object> knownContext
) {}