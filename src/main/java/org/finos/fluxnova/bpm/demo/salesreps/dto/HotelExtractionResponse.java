package org.finos.fluxnova.bpm.demo.salesreps.dto;

import java.util.List;
import java.util.Map;

public record HotelExtractionResponse(
        String intent,
        Double confidence,
        Boolean containsEmergency,
        String roomNumber,
        String language,
        List<String> missingFields,
        Map<String, Object> extractedEntities,
        Boolean requestComplete
) {}