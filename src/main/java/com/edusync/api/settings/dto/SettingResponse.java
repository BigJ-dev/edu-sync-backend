package com.edusync.api.settings.dto;

import com.edusync.api.settings.model.SystemSetting;

import java.time.Instant;
import java.util.UUID;

public record SettingResponse(
        String settingKey,
        String settingValue,
        String description,
        String category,
        UUID updatedByUuid,
        Instant createdAt,
        Instant updatedAt
) {
    public static SettingResponse from(SystemSetting setting) {
        return new SettingResponse(
                setting.getSettingKey(),
                setting.getSettingValue(),
                setting.getDescription(),
                setting.getCategory(),
                setting.getUpdatedBy() != null ? setting.getUpdatedBy().getUuid() : null,
                setting.getCreatedAt(),
                setting.getUpdatedAt()
        );
    }
}
