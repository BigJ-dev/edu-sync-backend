package com.edusync.api.aws.controller.api;

import com.edusync.api.aws.dto.EmailRequest;
import com.edusync.api.aws.dto.EmailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Email", description = "Endpoints for sending emails via AWS SES.")
@RequestMapping("/email")
public interface EmailApi {

    @PostMapping("/send")
    @Operation(
            summary = "Send an email",
            description = "Sends a single email to a recipient. Supports both plain text and HTML body content."
    )
    @ApiResponse(responseCode = "200", description = "Email sent successfully")
    @ApiResponse(responseCode = "502", description = "Failed to send email via AWS SES")
    EmailResponse sendEmail(@Valid @RequestBody EmailRequest.Send request);

    @PostMapping("/send-bulk")
    @Operation(
            summary = "Send bulk email",
            description = "Sends the same email to multiple recipients (up to 50). Supports both plain text and HTML body content."
    )
    @ApiResponse(responseCode = "200", description = "Bulk email sent successfully")
    @ApiResponse(responseCode = "502", description = "Failed to send one or more emails")
    List<EmailResponse> sendBulkEmail(@Valid @RequestBody EmailRequest.BulkSend request);
}
