package org.finos.fluxnova.bpm.demo.salesreps.dto;

import java.util.Map;

public record RoomServiceConfirmationEvaluationResponse(
        String confirmationAction,
        Map<String, Object> updatedOrder,
        String message
) {}
