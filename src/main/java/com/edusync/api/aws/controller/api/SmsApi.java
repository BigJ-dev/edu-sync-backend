package com.edusync.api.aws.controller.api;

import com.edusync.api.aws.dto.SmsRequest;
import com.edusync.api.aws.dto.SmsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "SMS", description = "Endpoints for sending SMS messages via AWS SNS.")
@RequestMapping("/sms")
public interface SmsApi {

    @PostMapping("/send")
    @Operation(
            summary = "Send an SMS",
            description = "Sends a single SMS message to a phone number in E.164 format (e.g. +27821234567)."
    )
    @ApiResponse(responseCode = "200", description = "SMS sent successfully")
    @ApiResponse(responseCode = "502", description = "Failed to send SMS via AWS SNS")
    SmsResponse sendSms(@Valid @RequestBody SmsRequest.Send request);

    @PostMapping("/send-bulk")
    @Operation(
            summary = "Send bulk SMS",
            description = "Sends the same SMS message to multiple phone numbers (up to 100)."
    )
    @ApiResponse(responseCode = "200", description = "Bulk SMS sent successfully")
    @ApiResponse(responseCode = "502", description = "Failed to send one or more SMS messages")
    List<SmsResponse> sendBulkSms(@Valid @RequestBody SmsRequest.BulkSend request);
}
