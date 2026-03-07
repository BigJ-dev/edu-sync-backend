package com.edusync.api.aws.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws.messaging")
@Getter
@Setter
public class MessagingProperties {

    private boolean enabled;
    private String region;
    private String fromEmail;
}
