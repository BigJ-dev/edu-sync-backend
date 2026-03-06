package com.edusync.api.course.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

public sealed interface CategoryRequest {

    @Schema(name = "CategoryCreate")
    record Create(
            @NotBlank(message = "Category name is required")
            @Size(max = 100, message = "Category name must not exceed 100 characters")
            String name,

            String description,

            UUID parentUuid,

            @Min(value = 0, message = "Sort order must be zero or positive")
            int sortOrder,

            @Size(max = 50, message = "Icon name must not exceed 50 characters")
            String iconName
    ) implements CategoryRequest {}

    @Schema(name = "CategoryUpdate")
    record Update(
            @NotBlank(message = "Category name is required")
            @Size(max = 100, message = "Category name must not exceed 100 characters")
            String name,

            String description,

            @Min(value = 0, message = "Sort order must be zero or positive")
            int sortOrder,

            @Size(max = 50, message = "Icon name must not exceed 50 characters")
            String iconName,

            boolean active
    ) implements CategoryRequest {}

    @Schema(name = "CategoryAssignCourse")
    record AssignCourse(
            @NotNull(message = "Course UUID is required")
            UUID courseUuid
    ) implements CategoryRequest {}
}
