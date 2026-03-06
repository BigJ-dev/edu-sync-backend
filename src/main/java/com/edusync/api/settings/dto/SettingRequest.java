package com.edusync.api.settings.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public sealed interface SettingRequest {

    @Schema(name = "SettingUpdate")
    record Update(
            @NotBlank(message = "Setting value is required")
            String settingValue,

            @NotNull(message = "Updated by UUID is required")
            UUID updatedByUuid
    ) implements SettingRequest {}
}
