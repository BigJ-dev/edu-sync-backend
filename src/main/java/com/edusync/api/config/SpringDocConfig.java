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
    GroupedOpenApi admissionApi() {
        return GroupedOpenApi.builder()
                .group("04. admissions")
                .pathsToMatch("/admissions/**", "/admissions")
                .build();
    }

    @Bean
    GroupedOpenApi studentApi() {
        return GroupedOpenApi.builder()
                .group("05. students")
                .pathsToMatch("/students/**")
                .build();
    }

    @Bean
    GroupedOpenApi courseApi() {
        return GroupedOpenApi.builder()
                .group("06. courses")
                .pathsToMatch("/courses/**")
                .build();
    }

    @Bean
    GroupedOpenApi classSessionApi() {
        return GroupedOpenApi.builder()
                .group("07. class-sessions")
                .pathsToMatch("/modules/**/class-sessions/**", "/modules/**/class-sessions")
                .build();
    }

    @Bean
    GroupedOpenApi attendanceApi() {
        return GroupedOpenApi.builder()
                .group("08. attendance")
                .pathsToMatch("/class-sessions/**/attendance/**", "/class-sessions/**/attendance")
                .build();
    }

    @Bean
    GroupedOpenApi materialApi() {
        return GroupedOpenApi.builder()
                .group("09. materials")
                .pathsToMatch("/modules/**/materials/**", "/modules/**/materials")
                .build();
    }

    @Bean
    GroupedOpenApi assessmentApi() {
        return GroupedOpenApi.builder()
                .group("10. assessments")
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
                .group("11. broadcasts")
                .pathsToMatch("/broadcasts/**", "/broadcasts")
                .build();
    }

    @Bean
    GroupedOpenApi supportApi() {
        return GroupedOpenApi.builder()
                .group("12. support")
                .pathsToMatch(
                        "/courses/**/support-threads/**", "/courses/**/support-threads",
                        "/support-threads/**/messages/**", "/support-threads/**/messages"
                )
                .build();
    }

    @Bean
    GroupedOpenApi notificationApi() {
        return GroupedOpenApi.builder()
                .group("13. notifications")
                .pathsToMatch("/notifications/**", "/notifications")
                .build();
    }

    @Bean
    GroupedOpenApi quizApi() {
        return GroupedOpenApi.builder()
                .group("14. quizzes")
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
                .group("15. student-groups")
                .pathsToMatch("/courses/**/groups/**", "/courses/**/groups")
                .build();
    }

    @Bean
    GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder()
                .group("16. categories")
                .pathsToMatch("/categories/**", "/categories")
                .build();
    }

    @Bean
    GroupedOpenApi certificateApi() {
        return GroupedOpenApi.builder()
                .group("17. certificates")
                .pathsToMatch("/certificates/**", "/certificates")
                .build();
    }

    @Bean
    GroupedOpenApi focusModeApi() {
        return GroupedOpenApi.builder()
                .group("18. focus-mode")
                .pathsToMatch("/focus-mode/**", "/focus-mode")
                .build();
    }

    @Bean
    GroupedOpenApi auditLogApi() {
        return GroupedOpenApi.builder()
                .group("19. audit-logs")
                .pathsToMatch("/audit-logs/**", "/audit-logs")
                .build();
    }

    @Bean
    GroupedOpenApi dashboardApi() {
        return GroupedOpenApi.builder()
                .group("20. dashboard")
                .pathsToMatch("/dashboard/**", "/dashboard")
                .build();
    }

    @Bean
    GroupedOpenApi timetableApi() {
        return GroupedOpenApi.builder()
                .group("21. timetable")
                .pathsToMatch("/timetable/**", "/timetable")
                .build();
    }

    @Bean
    GroupedOpenApi transcriptApi() {
        return GroupedOpenApi.builder()
                .group("22. transcripts")
                .pathsToMatch("/transcripts/**", "/transcripts")
                .build();
    }

    @Bean
    GroupedOpenApi settingsApi() {
        return GroupedOpenApi.builder()
                .group("23. settings")
                .pathsToMatch("/settings/**", "/settings")
                .build();
    }

    @Bean
    GroupedOpenApi s3Api() {
        return GroupedOpenApi.builder()
                .group("24. s3-storage")
                .pathsToMatch("/s3/**", "/s3")
                .build();
    }

    @Bean
    GroupedOpenApi smsApi() {
        return GroupedOpenApi.builder()
                .group("25. sms")
                .pathsToMatch("/sms/**", "/sms")
                .build();
    }

    @Bean
    GroupedOpenApi emailApi() {
        return GroupedOpenApi.builder()
                .group("26. email")
                .pathsToMatch("/email/**", "/email")
                .build();
    }

    @Bean
    GroupedOpenApi teamsApi() {
        return GroupedOpenApi.builder()
                .group("27. teams")
                .pathsToMatch("/teams/**", "/teams")
                .build();
    }

    @Bean
    GroupedOpenApi announcementApi() {
        return GroupedOpenApi.builder()
                .group("28. announcements")
                .pathsToMatch("/announcements/**", "/announcements")
                .build();
    }

    @Bean
    GroupedOpenApi gradingApi() {
        return GroupedOpenApi.builder()
                .group("29. grading")
                .pathsToMatch("/courses/**/grading/**")
                .build();
    }

    @Bean
    GroupedOpenApi workOverviewApi() {
        return GroupedOpenApi.builder()
                .group("30. work-overview")
                .pathsToMatch("/work/**")
                .build();
    }
}
