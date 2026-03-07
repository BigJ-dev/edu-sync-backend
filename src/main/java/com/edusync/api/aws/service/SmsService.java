package com.edusync.api.aws.service;

import com.edusync.api.aws.dto.SmsRequest;
import com.edusync.api.aws.dto.SmsResponse;

import java.util.List;

public interface SmsService {

    SmsResponse sendSms(SmsRequest.Send request);

    List<SmsResponse> sendBulkSms(SmsRequest.BulkSend request);
}
