package org.finos.fluxnova.bpm.demo.salesreps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AgentClientConfig {

    @Bean
    public RestClient hotelAgentRestClient() {
        return RestClient.builder()
                .baseUrl("https://hotel-ai-agent-471r.onrender.com")
                //.baseUrl("http://127.0.0.1:8000")
                .build();
    }
}
