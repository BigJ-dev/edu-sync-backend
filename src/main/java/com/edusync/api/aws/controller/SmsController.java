package com.edusync.api.aws.controller;

import com.edusync.api.aws.controller.api.SmsApi;
import com.edusync.api.aws.dto.SmsRequest;
import com.edusync.api.aws.dto.SmsResponse;
import com.edusync.api.aws.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@ConditionalOnProperty(prefix = "aws.messaging", name = "enabled", havingValue = "true")
public class SmsController implements SmsApi {

    private final SmsService service;

    @Override
    public SmsResponse sendSms(SmsRequest.Send request) {
        return service.sendSms(request);
    }

    @Override
    public List<SmsResponse> sendBulkSms(SmsRequest.BulkSend request) {
        return service.sendBulkSms(request);
    }
}
