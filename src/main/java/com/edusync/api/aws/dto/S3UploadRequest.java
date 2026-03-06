package com.edusync.api.aws.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "S3UploadRequest")
public record S3UploadRequest(

        @NotBlank(message = "Bucket path is required")
        @Schema(description = "Storage bucket path for the upload", example = "assignments")
        @Size(max = 400, message = "Bucket must not exceed 400 characters")
        String bucket,

        @NotBlank(message = "File name is required")
        @Schema(description = "Original file name", example = "chapter-1-notes.pdf")
        @Size(max = 255, message = "File name must not exceed 255 characters")
        String fileName,

        @Schema(description = "MIME type of the file", example = "application/pdf")
        @Size(max = 100, message = "Content type must not exceed 100 characters")
        String contentType
) {}
