package org.finos.fluxnova.bpm.demo.salesreps.web;

import org.finos.fluxnova.bpm.demo.salesreps.service.WhatsappMessageStore;
import org.finos.fluxnova.bpm.engine.RuntimeService;
import org.finos.fluxnova.bpm.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/whatsapp")
public class WhatsappDemoController {

    private final RuntimeService runtimeService;
    private final WhatsappMessageStore messageStore;

    public WhatsappDemoController(
            RuntimeService runtimeService,
            WhatsappMessageStore messageStore
    ) {
        this.runtimeService = runtimeService;
        this.messageStore = messageStore;
    }

    @PostMapping("/start")
    public Map<String, Object> startConversation(@RequestBody Map<String, Object> body) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(
                "hotelGuestAssistantProcess",
                body
        );

        String processInstanceId = instance.getId();

        String response = messageStore.get(processInstanceId);

        Map<String, Object> result = new HashMap<>();
        result.put("processInstanceId", processInstanceId);
        result.put("message", response != null ? response : "Request completed.");
        result.put("completed", response == null);

        return result;
    }

    @PostMapping("/reply")
    public Map<String, Object> receiveGuestReply(@RequestBody Map<String, Object> body) {
        String processInstanceId = body.get("processInstanceId").toString();
        String guestMessage = body.get("guestMessage").toString();

        runtimeService.createMessageCorrelation("GuestReplyReceived")
                .processInstanceId(processInstanceId)
                .setVariable("guestMessage", guestMessage)
                .correlate();

        String response = messageStore.get(processInstanceId);

        Map<String, Object> result = new HashMap<>();
        result.put("processInstanceId", processInstanceId);
        result.put("message", response != null ? response : "Request completed.");
        result.put("completed", response == null);

        return result;
    }
}