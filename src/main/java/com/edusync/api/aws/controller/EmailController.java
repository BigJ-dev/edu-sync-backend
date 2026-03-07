package com.edusync.api.aws.controller;

import com.edusync.api.aws.controller.api.EmailApi;
import com.edusync.api.aws.dto.EmailRequest;
import com.edusync.api.aws.dto.EmailResponse;
import com.edusync.api.aws.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@ConditionalOnProperty(prefix = "aws.messaging", name = "enabled", havingValue = "true")
public class EmailController implements EmailApi {

    private final EmailService service;

    @Override
    public EmailResponse sendEmail(EmailRequest.Send request) {
        return service.sendEmail(request);
    }

    @Override
    public List<EmailResponse> sendBulkEmail(EmailRequest.BulkSend request) {
        return service.sendBulkEmail(request);
    }
}
