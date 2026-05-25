package org.finos.fluxnova.bpm.demo.salesreps.dto;

import java.util.List;
import java.util.Map;

public record RoomServiceConfirmationRequest(
        Map<String, Object> extraction,
        List<ConversationMessageDto> conversationHistory
) {}
