package com.edusync.api.course.material.dto;

import com.edusync.api.course.material.enums.MaterialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface MaterialRequest {

    @Schema(name = "MaterialCreate")
    record Create(
            @NotBlank(message = _MATERIAL_TITLE_REQUIRED)
            @Size(max = 255, message = _MATERIAL_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _MATERIAL_DESCRIPTION_SIZE)
            String description,

            @NotNull(message = _MATERIAL_TYPE_REQUIRED)
            MaterialType materialType,

            @NotNull(message = _MATERIAL_UPLOADED_BY_REQUIRED)
            UUID uploadedByUuid,

            UUID classSessionUuid,

            @Size(max = 500, message = _MATERIAL_S3_KEY_SIZE)
            String s3Key,

            @Size(max = 1000, message = _MATERIAL_EXTERNAL_URL_SIZE)
            String externalUrl,

            @Size(max = 255, message = _MATERIAL_FILE_NAME_SIZE)
            String fileName,

            Long fileSizeBytes,

            @Size(max = 100, message = _MATERIAL_MIME_TYPE_SIZE)
            String mimeType,

            Integer sortOrder,

            Boolean visibleToStudents
    ) implements MaterialRequest {}

    @Schema(name = "MaterialUpdate")
    record Update(
            @NotBlank(message = _MATERIAL_TITLE_REQUIRED)
            @Size(max = 255, message = _MATERIAL_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _MATERIAL_DESCRIPTION_SIZE)
            String description,

            @Size(max = 500, message = _MATERIAL_S3_KEY_SIZE)
            String s3Key,

            @Size(max = 1000, message = _MATERIAL_EXTERNAL_URL_SIZE)
            String externalUrl,

            @Size(max = 255, message = _MATERIAL_FILE_NAME_SIZE)
            String fileName,

            Long fileSizeBytes,

            @Size(max = 100, message = _MATERIAL_MIME_TYPE_SIZE)
            String mimeType,

            Integer sortOrder,

            Boolean visibleToStudents
    ) implements MaterialRequest {}
}
