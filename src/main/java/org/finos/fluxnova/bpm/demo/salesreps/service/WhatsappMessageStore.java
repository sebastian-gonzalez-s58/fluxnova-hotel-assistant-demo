package org.finos.fluxnova.bpm.demo.salesreps.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WhatsappMessageStore {

    private final Map<String, String> messages = new ConcurrentHashMap<>();

    public void save(String processInstanceId, String message) {
        messages.put(processInstanceId, message);
    }

    public String get(String processInstanceId) {
        return messages.get(processInstanceId);
    }
}
