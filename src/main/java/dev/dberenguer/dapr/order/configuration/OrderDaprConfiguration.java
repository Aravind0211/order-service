package dev.dberenguer.dapr.order.configuration;

import io.dapr.client.DaprClient;
import io.dapr.exceptions.DaprException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderDaprConfiguration {

    @Value("${dapr.port:3500}")
    private int daprPort;

    @Bean
    public String secretDaprClient() {
        try {
            DaprClient daprClient = new DaprClientBuilder().build();
            String secret = daprClient.getSecret("my-secret-store", "my-secret-name").block(); // Replace with your secret store and secret name
            return secret;
        } catch (DaprException ex) {
            throw new RuntimeException("Failed to initialize secretDaprClient", ex);
        }
    }
}
