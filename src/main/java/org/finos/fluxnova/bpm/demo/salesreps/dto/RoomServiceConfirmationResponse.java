package org.finos.fluxnova.bpm.demo.salesreps.dto;

import java.util.Map;

public record RoomServiceConfirmationResponse(
        String message,
        Map<String, Object> pendingOrder
) {}
