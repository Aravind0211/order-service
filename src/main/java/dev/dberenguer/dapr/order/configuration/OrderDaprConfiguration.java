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
            // Log DaprException with specific details
            log.error("DaprException occurred while fetching secret from Dapr. Message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch secret from Dapr", e);
        } catch (Exception e) {
            // Log other exceptions
            log.error("Exception occurred while fetching secret from Dapr", e);
            throw new RuntimeException("Failed to fetch secret from Dapr", e);
        }
    }
}
