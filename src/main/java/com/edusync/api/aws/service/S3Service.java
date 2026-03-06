package com.edusync.api.aws.service;

import com.edusync.api.aws.config.S3Properties;
import com.edusync.api.aws.dto.PresignedUrlResponse;
import com.edusync.api.aws.dto.S3UploadRequest;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.settings.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import com.edusync.api.aws.enums.HttpMethod;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3Properties properties;
    private final SystemSettingService settingService;

    public PresignedUrlResponse generateUploadUrl(S3UploadRequest request) {
        var contentType = request.contentType() != null ? request.contentType() : "application/octet-stream";
        validateContentType(contentType);

        var key = buildKey(request.bucket(), request.fileName());
        Duration timeToLive = getPresignedTimeToLive();

        var putObjectRequest = PutObjectRequest.builder()
                .bucket(getBucket())
                .key(key)
                .contentType(contentType)
                .contentLength(getMaxFileSize())
                .build();

        var presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(timeToLive)
                .putObjectRequest(putObjectRequest)
                .build();

        var presigned = s3Presigner.presignPutObject(presignRequest);

        return new PresignedUrlResponse(
                presigned.url().toString(),
                key,
                HttpMethod.PUT,
                Instant.now().plus(timeToLive)
        );
    }

    public PresignedUrlResponse generateDownloadUrl(String key) {
        verifyObjectExists(key);

        Duration timeToLive = getPresignedTimeToLive();

        var getObjectRequest = GetObjectRequest.builder()
                .bucket(getBucket())
                .key(key)
                .build();

        var presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(timeToLive)
                .getObjectRequest(getObjectRequest)
                .build();

        var presigned = s3Presigner.presignGetObject(presignRequest);

        return new PresignedUrlResponse(
                presigned.url().toString(),
                key,
                HttpMethod.GET,
                Instant.now().plus(timeToLive)
        );
    }

    public void deleteObject(String key) {
        var request = DeleteObjectRequest.builder()
                .bucket(getBucket())
                .key(key)
                .build();

        s3Client.deleteObject(request);
    }

    private String getBucket() {
        return properties.getBucket();
    }

    private Duration getPresignedTimeToLive() {
        var minutes = Long.parseLong(settingService.getValue("presigned_url_ttl_minutes"));
        return Duration.ofMinutes(minutes);
    }

    private long getMaxFileSize() {
        return Long.parseLong(settingService.getValue("max_file_upload_bytes"));
    }

    private Set<String> getAllowedMimeTypes() {
        var raw = settingService.getValue("allowed_upload_mime_types");
        return Set.of(raw.split(","));
    }

    private void validateContentType(String contentType) {
        var allowed = getAllowedMimeTypes();
        if (!allowed.contains(contentType)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "File type '" + contentType + "' is not allowed. Allowed types: " + String.join(", ", allowed));
        }
    }

    private String buildKey(String bucket, String fileName) {
        var sanitizedBucket = bucket.replaceAll("^/+|/+$", "");
        var uniquePrefix = UUID.randomUUID().toString().substring(0, 8);
        return sanitizedBucket + "/" + uniquePrefix + "-" + fileName;
    }

    private void verifyObjectExists(String key) {
        try {
            var request = HeadObjectRequest.builder()
                    .bucket(getBucket())
                    .key(key)
                    .build();

            s3Client.headObject(request);
        } catch (NoSuchKeyException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Object not found: " + key);
        }
    }
}
