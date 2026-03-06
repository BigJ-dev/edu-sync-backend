package com.edusync.api.course.group.dto;

import com.edusync.api.course.group.enums.GroupMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

public sealed interface GroupRequest {

    @Schema(name = "GroupCreate")
    record Create(
            @NotNull(message = "Course UUID is required")
            UUID courseUuid,

            UUID moduleUuid,

            @NotBlank(message = "Group name is required")
            @Size(max = 100, message = "Group name must not exceed 100 characters")
            String name,

            String description,

            @Min(value = 1, message = "Max members must be at least 1")
            Integer maxMembers,

            @NotNull(message = "Created by UUID is required")
            UUID createdByUuid
    ) implements GroupRequest {}

    @Schema(name = "GroupUpdate")
    record Update(
            @NotBlank(message = "Group name is required")
            @Size(max = 100, message = "Group name must not exceed 100 characters")
            String name,

            String description,

            @Min(value = 1, message = "Max members must be at least 1")
            Integer maxMembers
    ) implements GroupRequest {}

    @Schema(name = "GroupAddMember")
    record AddMember(
            @NotNull(message = "Student UUID is required")
            UUID studentUuid,

            GroupMemberRole role
    ) implements GroupRequest {}

    @Schema(name = "GroupUpdateMemberRole")
    record UpdateMemberRole(
            @NotNull(message = "Role is required")
            GroupMemberRole role
    ) implements GroupRequest {}
}
