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
                .group("01. all")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("02. admins")
                .pathsToMatch("/admins/**")
                .build();
    }

    @Bean
    GroupedOpenApi lecturerApi() {
        return GroupedOpenApi.builder()
                .group("03. lecturers")
                .pathsToMatch("/lecturers/**")
                .build();
    }

    @Bean
    GroupedOpenApi studentApi() {
        return GroupedOpenApi.builder()
                .group("04. students")
                .pathsToMatch("/students/**")
                .build();
    }

    @Bean
    GroupedOpenApi courseApi() {
        return GroupedOpenApi.builder()
                .group("05. courses")
                .pathsToMatch("/courses/**")
                .build();
    }

    @Bean
    GroupedOpenApi classSessionApi() {
        return GroupedOpenApi.builder()
                .group("06. class-sessions")
                .pathsToMatch("/modules/**/class-sessions/**", "/modules/**/class-sessions")
                .build();
    }

    @Bean
    GroupedOpenApi attendanceApi() {
        return GroupedOpenApi.builder()
                .group("07. attendance")
                .pathsToMatch("/class-sessions/**/attendance/**", "/class-sessions/**/attendance")
                .build();
    }

    @Bean
    GroupedOpenApi materialApi() {
        return GroupedOpenApi.builder()
                .group("08. materials")
                .pathsToMatch("/modules/**/materials/**", "/modules/**/materials")
                .build();
    }

    @Bean
    GroupedOpenApi assessmentApi() {
        return GroupedOpenApi.builder()
                .group("09. assessments")
                .pathsToMatch(
                        "/modules/**/assessments/**", "/modules/**/assessments",
                        "/assessments/**/rubric/**", "/assessments/**/rubric",
                        "/assessments/**/submissions/**", "/assessments/**/submissions",
                        "/submissions/**/grades/**", "/submissions/**/grades"
                )
                .build();
    }

    @Bean
    GroupedOpenApi broadcastApi() {
        return GroupedOpenApi.builder()
                .group("10. broadcasts")
                .pathsToMatch("/broadcasts/**", "/broadcasts")
                .build();
    }

    @Bean
    GroupedOpenApi messagingApi() {
        return GroupedOpenApi.builder()
                .group("11. messaging")
                .pathsToMatch(
                        "/courses/**/threads/**", "/courses/**/threads",
                        "/threads/**/messages/**", "/threads/**/messages"
                )
                .build();
    }

    @Bean
    GroupedOpenApi notificationApi() {
        return GroupedOpenApi.builder()
                .group("12. notifications")
                .pathsToMatch("/notifications/**", "/notifications")
                .build();
    }

    @Bean
    GroupedOpenApi quizApi() {
        return GroupedOpenApi.builder()
                .group("13. quizzes")
                .pathsToMatch(
                        "/modules/**/quizzes/**", "/modules/**/quizzes",
                        "/quizzes/**/questions/**", "/quizzes/**/questions",
                        "/quizzes/**/attempts/**", "/quizzes/**/attempts",
                        "/attempts/**/answers/**", "/attempts/**/answers"
                )
                .build();
    }

    @Bean
    GroupedOpenApi groupApi() {
        return GroupedOpenApi.builder()
                .group("14. student-groups")
                .pathsToMatch("/courses/**/groups/**", "/courses/**/groups")
                .build();
    }

    @Bean
    GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder()
                .group("15. categories")
                .pathsToMatch("/categories/**", "/categories")
                .build();
    }

    @Bean
    GroupedOpenApi certificateApi() {
        return GroupedOpenApi.builder()
                .group("16. certificates")
                .pathsToMatch("/certificates/**", "/certificates")
                .build();
    }

    @Bean
    GroupedOpenApi focusModeApi() {
        return GroupedOpenApi.builder()
                .group("17. focus-mode")
                .pathsToMatch("/focus-mode/**", "/focus-mode")
                .build();
    }

    @Bean
    GroupedOpenApi auditLogApi() {
        return GroupedOpenApi.builder()
                .group("18. audit-logs")
                .pathsToMatch("/audit-logs/**", "/audit-logs")
                .build();
    }

    @Bean
    GroupedOpenApi settingsApi() {
        return GroupedOpenApi.builder()
                .group("19. settings")
                .pathsToMatch("/settings/**", "/settings")
                .build();
    }

    @Bean
    GroupedOpenApi s3Api() {
        return GroupedOpenApi.builder()
                .group("20. s3-storage")
                .pathsToMatch("/s3/**", "/s3")
                .build();
    }

    @Bean
    GroupedOpenApi teamsApi() {
        return GroupedOpenApi.builder()
                .group("21. teams")
                .pathsToMatch("/teams/**", "/teams")
                .build();
    }
}
