package com.edusync.api.teams.config;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "azure.ad", name = {"tenant-id", "client-id", "client-secret"})
public class GraphClientConfig {

    private final AzureAdProperties properties;

    @Bean
    public GraphServiceClient graphServiceClient() {
        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .tenantId(properties.getTenantId())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .build();

        return new GraphServiceClient(credential, "https://graph.microsoft.com/.default");
    }
}
