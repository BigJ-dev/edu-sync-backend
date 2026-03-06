package com.edusync.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "EduSync API",
                version = "1.0.0",
                contact = @Contact(name = "EduSync Support", email = "support@edusync.co.za")
        )
)
public class SpringDocConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("EduSync API")
                        .version("1.0.0")
                        .description("EduSync is a comprehensive learning management platform that handles administrator oversight, " +
                                "lecturer management, student enrollment, course delivery, academic progress tracking, " +
                                "profile management, and AWS Cognito authentication integration for seamless " +
                                "educational institution operations.")
                );
    }

    @Bean
    GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("1. all")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("2. admins")
                .pathsToMatch("/admins/**")
                .build();
    }

    @Bean
    GroupedOpenApi lecturerApi() {
        return GroupedOpenApi.builder()
                .group("3. lecturers")
                .pathsToMatch("/lecturers/**")
                .build();
    }

    @Bean
    GroupedOpenApi studentApi() {
        return GroupedOpenApi.builder()
                .group("4. students")
                .pathsToMatch("/students/**")
                .build();
    }
}
