package com.edusync.api.aws.controller.api;

import com.edusync.api.aws.dto.PresignedUrlResponse;
import com.edusync.api.aws.dto.S3UploadRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "S3 Storage", description = "Endpoints for uploading, retrieving, and deleting files via AWS S3 presigned URLs.")
@RequestMapping("/s3")
public interface S3Api {

    @PostMapping("/upload-url")
    @Operation(
            summary = "Generate a presigned upload URL",
            description = "Generates a presigned PUT URL for the client to upload a file directly to S3. " +
                    "The client should use the returned URL with an HTTP PUT request to upload the file."
    )
    @ApiResponse(responseCode = "200", description = "Presigned upload URL generated successfully")
    PresignedUrlResponse generateS3UploadUrl(@Valid @RequestBody S3UploadRequest request);

    @GetMapping("/download-url")
    @Operation(
            summary = "Generate a presigned download URL",
            description = "Generates a presigned GET URL for the client to download a file from S3. " +
                    "Verifies the object exists before generating the URL."
    )
    @ApiResponse(responseCode = "200", description = "Presigned download URL generated successfully")
    @ApiResponse(responseCode = "404", description = "Object not found in S3")
    PresignedUrlResponse generateS3DownloadUrl(@RequestParam String key);

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete an object from S3",
            description = "Deletes an object from the S3 bucket by its key."
    )
    @ApiResponse(responseCode = "204", description = "Object deleted successfully")
    void deleteS3Object(@RequestParam String key);
}
