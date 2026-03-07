package com.edusync.api.aws.service;

import com.edusync.api.aws.dto.EmailRequest;
import com.edusync.api.aws.dto.EmailResponse;

import java.util.List;

public interface EmailService {

    EmailResponse sendEmail(EmailRequest.Send request);

    List<EmailResponse> sendBulkEmail(EmailRequest.BulkSend request);
}
