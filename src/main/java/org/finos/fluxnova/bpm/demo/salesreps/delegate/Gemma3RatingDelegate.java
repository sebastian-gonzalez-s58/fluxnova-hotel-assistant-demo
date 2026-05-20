package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Component;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

@Component("gemma3RatingDelegate")
public class Gemma3RatingDelegate implements JavaDelegate {

    private final ChatModel chatModel;

    public Gemma3RatingDelegate(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public void execute(DelegateExecution execution) {

        String prompt = "Hello Gemma3. Please respond with a short greeting.";

        String response = chatModel.chat(prompt);

        System.out.println("LLM response: " + response);

        execution.setVariable("llmResponse", response);
    }
}