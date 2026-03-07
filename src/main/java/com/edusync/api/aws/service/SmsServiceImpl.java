package com.edusync.api.aws.service;

import com.edusync.api.aws.dto.SmsRequest;
import com.edusync.api.aws.dto.SmsResponse;
import com.edusync.api.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "aws.messaging", name = "enabled", havingValue = "true")
public class SmsServiceImpl implements SmsService {

    private final SnsClient snsClient;

    @Override
    public SmsResponse sendSms(SmsRequest.Send request) {
        try {
            var publishRequest = PublishRequest.builder()
                    .phoneNumber(request.phoneNumber())
                    .message(request.message())
                    .build();

            var result = snsClient.publish(publishRequest);

            log.info("SMS sent to {} with messageId {}", request.phoneNumber(), result.messageId());
            return new SmsResponse(request.phoneNumber(), result.messageId(), "SENT");
        } catch (SnsException e) {
            log.error("Failed to send SMS to {}: {}", request.phoneNumber(), e.getMessage());
            throw new ServiceException(HttpStatus.BAD_GATEWAY, "Failed to send SMS: " + e.getMessage());
        }
    }

    @Override
    public List<SmsResponse> sendBulkSms(SmsRequest.BulkSend request) {
        return request.phoneNumbers().stream()
                .map(phone -> sendSms(new SmsRequest.Send(phone, request.message())))
                .toList();
    }
}
