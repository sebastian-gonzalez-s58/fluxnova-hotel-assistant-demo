package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import org.finos.fluxnova.bpm.demo.salesreps.service.ConversationVariableMapper;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("housekeepingDelegate")
public class HousekeepingDelegate implements JavaDelegate {

    private final ConversationVariableMapper mapper;

    public HousekeepingDelegate(ConversationVariableMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String roomNumber = mapper.getString(execution, "roomNumber");
        Map<String, Object> entities = mapper.getMap(execution, "extractedEntities");

        String service = getServiceDescription(entities);

        String response = "Housekeeping has been notified"
                + (roomNumber != null ? " for room " + roomNumber : "")
                + (service != null ? " about " + service : "")
                + ". A staff member will deliver it shortly.";

        execution.setVariable("housekeepingRequest", entities);
        execution.setVariable("outgoingWhatsappMessage", response);
        execution.setVariable("lastAssistantMessage", response);
    }

    private String getServiceDescription(Map<String, Object> entities) {
        Object requestedItemsOrService = entities.get("requestedItemsOrService");
        if (requestedItemsOrService != null) {
            return formatValue(requestedItemsOrService);
        }

        Object requestedItems = entities.get("requestedItems");
        if (requestedItems != null) {
            return formatValue(requestedItems);
        }

        Object items = entities.get("items");
        if (items != null) {
            return formatValue(items);
        }

        return null;
    }

    private String formatValue(Object value) {
        if (value instanceof List<?> values) {
            return values.stream()
                    .map(this::formatValue)
                    .filter(item -> item != null && !item.isBlank())
                    .reduce((left, right) -> left + ", " + right)
                    .orElse(null);
        }

        if (value instanceof Map<?, ?> item) {
            Object name = item.get("name");
            Object quantity = item.get("quantity");

            if (name != null && quantity != null) {
                return quantity + " " + name;
            }

            if (name != null) {
                return name.toString();
            }
        }

        return value == null ? null : value.toString();
    }
}
