package com.edusync.api.course.group.controller.api;

import com.edusync.api.course.group.dto.GroupMemberResponse;
import com.edusync.api.course.group.dto.GroupRequest;
import com.edusync.api.course.group.dto.GroupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Student Groups", description = "Endpoints for managing student groups within a course. Groups allow students to collaborate on assignments and projects.")
@RequestMapping("/courses/{courseUuid}/groups")
public interface CourseGroupApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new group",
            description = "Creates a new student group within a course. Optionally linked to a specific module."
    )
    @ApiResponse(responseCode = "201", description = "Group created successfully")
    @ApiResponse(responseCode = "404", description = "Course, module, or user not found")
    @ApiResponse(responseCode = "409", description = "Group name already exists in this course and module")
    GroupResponse create(@PathVariable UUID courseUuid, @Valid @RequestBody GroupRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all groups for a course",
            description = "Returns all groups belonging to a course. Supports searching by group name."
    )
    @ApiResponse(responseCode = "200", description = "Groups retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    List<GroupResponse> findAllByCourse(
            @PathVariable UUID courseUuid,
            @RequestParam(required = false) String search);

    @GetMapping("/{groupUuid}")
    @Operation(
            summary = "Get group by UUID",
            description = "Retrieves a single group's details by its unique identifier."
    )
    @ApiResponse(responseCode = "200", description = "Group retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Group not found")
    GroupResponse findByUuid(@PathVariable UUID courseUuid, @PathVariable UUID groupUuid);

    @PutMapping("/{groupUuid}")
    @Operation(
            summary = "Update group details",
            description = "Updates a group's name, description, or maximum members."
    )
    @ApiResponse(responseCode = "200", description = "Group updated successfully")
    @ApiResponse(responseCode = "404", description = "Group not found")
    @ApiResponse(responseCode = "409", description = "Group name already exists in this course and module")
    GroupResponse update(@PathVariable UUID courseUuid, @PathVariable UUID groupUuid, @Valid @RequestBody GroupRequest.Update request);

    @DeleteMapping("/{groupUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a group",
            description = "Deletes a group and all its member associations."
    )
    @ApiResponse(responseCode = "204", description = "Group deleted successfully")
    @ApiResponse(responseCode = "404", description = "Group not found")
    void delete(@PathVariable UUID courseUuid, @PathVariable UUID groupUuid);

    @PostMapping("/{groupUuid}/members")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add a member to a group",
            description = "Adds a student to the group. Validates maximum member limit and prevents duplicate membership."
    )
    @ApiResponse(responseCode = "201", description = "Member added successfully")
    @ApiResponse(responseCode = "404", description = "Group or student not found")
    @ApiResponse(responseCode = "409", description = "Student is already a member or group is full")
    GroupMemberResponse addMember(@PathVariable UUID courseUuid, @PathVariable UUID groupUuid, @Valid @RequestBody GroupRequest.AddMember request);

    @GetMapping("/{groupUuid}/members")
    @Operation(
            summary = "List all members of a group",
            description = "Returns all students who are members of the specified group."
    )
    @ApiResponse(responseCode = "200", description = "Members retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Group not found")
    List<GroupMemberResponse> findMembers(@PathVariable UUID courseUuid, @PathVariable UUID groupUuid);

    @DeleteMapping("/{groupUuid}/members/{studentUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Remove a member from a group",
            description = "Removes a student from the group."
    )
    @ApiResponse(responseCode = "204", description = "Member removed successfully")
    @ApiResponse(responseCode = "404", description = "Group or member not found")
    void removeMember(@PathVariable UUID courseUuid, @PathVariable UUID groupUuid, @PathVariable UUID studentUuid);

    @PatchMapping("/{groupUuid}/members/{studentUuid}/role")
    @Operation(
            summary = "Update a member's role",
            description = "Changes a group member's role (e.g. MEMBER to LEADER)."
    )
    @ApiResponse(responseCode = "200", description = "Member role updated successfully")
    @ApiResponse(responseCode = "404", description = "Group or member not found")
    GroupMemberResponse updateMemberRole(@PathVariable UUID courseUuid, @PathVariable UUID groupUuid, @PathVariable UUID studentUuid, @Valid @RequestBody GroupRequest.UpdateMemberRole request);
}
