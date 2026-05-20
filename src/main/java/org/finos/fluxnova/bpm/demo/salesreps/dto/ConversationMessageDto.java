package org.finos.fluxnova.bpm.demo.salesreps.dto;

public record ConversationMessageDto(
        String role,
        String content
) {}