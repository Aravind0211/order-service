package dev.dberenguer.dapr.order.configuration;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.exceptions.DaprException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
public class OrderDaprConfiguration {

    @Value("${dberenguer.dapr.secret-store.name}")
    private String secretStoreName;

    @Bean
    public DaprClient daprClient() {
        return new DaprClientBuilder().build();
    }

    @Bean
    public String secretDaprClient(DaprClient daprClient) {
        try {
            // Attempt to fetch the secret
            Map<String, String> secret = daprClient.getSecret(this.secretStoreName, "mySecret").block();
            log.info("Fetched Secret: {}", secret);
            return secret.toString();
        } catch (DaprException e) {
            // Log the exception and attempt a retry or handle accordingly
            log.error("Failed to fetch secret from Dapr", e);
            // You can implement retry logic here if needed
            throw new RuntimeException("Failed to fetch secret from Dapr", e);
        } catch (Exception e) {
            // Catch any other unexpected exceptions and handle them
            log.error("Unexpected error fetching secret from Dapr", e);
            throw new RuntimeException("Unexpected error fetching secret from Dapr", e);
        }
    }
}
