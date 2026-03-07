package com.edusync.api.aws.service;

import com.edusync.api.aws.config.MessagingProperties;
import com.edusync.api.aws.dto.EmailRequest;
import com.edusync.api.aws.dto.EmailResponse;
import com.edusync.api.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "aws.messaging", name = "enabled", havingValue = "true")
public class EmailServiceImpl implements EmailService {

    private final SesClient sesClient;
    private final MessagingProperties properties;

    @Override
    public EmailResponse sendEmail(EmailRequest.Send request) {
        try {
            var body = request.html()
                    ? Body.builder().html(Content.builder().data(request.body()).charset(StandardCharsets.UTF_8.name()).build()).build()
                    : Body.builder().text(Content.builder().data(request.body()).charset(StandardCharsets.UTF_8.name()).build()).build();

            var message = Message.builder()
                    .subject(Content.builder().data(request.subject()).charset(StandardCharsets.UTF_8.name()).build())
                    .body(body)
                    .build();

            var sendRequest = SendEmailRequest.builder()
                    .source(properties.getFromEmail())
                    .destination(Destination.builder().toAddresses(request.to()).build())
                    .message(message)
                    .build();

            var result = sesClient.sendEmail(sendRequest);

            log.info("Email sent to {} with messageId {}", request.to(), result.messageId());
            return new EmailResponse(request.to(), result.messageId(), "SENT");
        } catch (SesException e) {
            log.error("Failed to send email to {}: {}", request.to(), e.getMessage());
            throw new ServiceException(HttpStatus.BAD_GATEWAY, "Failed to send email: " + e.getMessage());
        }
    }

    @Override
    public List<EmailResponse> sendBulkEmail(EmailRequest.BulkSend request) {
        return request.to().stream()
                .map(recipient -> sendEmail(new EmailRequest.Send(recipient, request.subject(), request.body(), request.html())))
                .toList();
    }
}
