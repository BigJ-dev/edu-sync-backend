package com.edusync.api.aws.service;

import com.edusync.api.aws.dto.PresignedUrlResponse;
import com.edusync.api.aws.dto.S3UploadRequest;

public interface S3Service {

    PresignedUrlResponse generateS3UploadUrl(S3UploadRequest request);

    PresignedUrlResponse generateS3DownloadUrl(String key);

    void deleteS3Object(String key);
}
