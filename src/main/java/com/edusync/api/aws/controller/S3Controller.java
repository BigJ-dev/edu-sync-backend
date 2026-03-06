package com.edusync.api.aws.controller;

import com.edusync.api.aws.controller.api.S3Api;
import com.edusync.api.aws.dto.PresignedUrlResponse;
import com.edusync.api.aws.dto.S3UploadRequest;
import com.edusync.api.aws.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class S3Controller implements S3Api {

    private final S3Service service;

    @Override
    public PresignedUrlResponse generateUploadUrl(S3UploadRequest request) {
        return service.generateUploadUrl(request);
    }

    @Override
    public PresignedUrlResponse generateDownloadUrl(String key) {
        return service.generateDownloadUrl(key);
    }

    @Override
    public void deleteObject(String key) {
        service.deleteObject(key);
    }
}
