package com.edusync.api.course.module.dto;

import com.edusync.api.course.module.enums.ModuleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface ModuleRequest {

    @Schema(name = "ModuleCreate")
    record Create(
            @NotBlank(message = _MODULE_TITLE_REQUIRED)
            @Size(max = 255, message = _MODULE_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _MODULE_DESCRIPTION_SIZE)
            String description,

            @NotNull(message = _MODULE_SORT_ORDER_REQUIRED)
            @Min(value = 0, message = _MODULE_SORT_ORDER_MIN)
            Integer sortOrder,

            LocalDate startDate,

            LocalDate endDate
    ) implements ModuleRequest {}

    @Schema(name = "ModuleUpdate")
    record Update(
            @NotBlank(message = _MODULE_TITLE_REQUIRED)
            @Size(max = 255, message = _MODULE_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _MODULE_DESCRIPTION_SIZE)
            String description,

            @NotNull(message = _MODULE_SORT_ORDER_REQUIRED)
            @Min(value = 0, message = _MODULE_SORT_ORDER_MIN)
            Integer sortOrder,

            LocalDate startDate,

            LocalDate endDate
    ) implements ModuleRequest {}

    @Schema(name = "ModuleUpdateStatus")
    record UpdateStatus(
            @NotNull(message = _MODULE_STATUS_REQUIRED)
            ModuleStatus status
    ) implements ModuleRequest {}
}
