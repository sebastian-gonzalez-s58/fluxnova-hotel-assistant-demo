package org.finos.fluxnova.bpm.demo.salesreps.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LlmConfig {

    @Bean
    public ChatModel chatModel() {
        return OpenAiChatModel.builder()
                .baseUrl("http://localhost:11434/v1")
                .apiKey("ollama")
                .modelName("gemma3")
                .temperature(0.2)
                .build();
    }
}