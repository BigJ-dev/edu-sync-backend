package com.edusync.api.course.broadcast.controller.api;

import com.edusync.api.course.broadcast.dto.BroadcastRecipientResponse;
import com.edusync.api.course.broadcast.dto.BroadcastRequest;
import com.edusync.api.course.broadcast.dto.BroadcastResponse;
import com.edusync.api.course.broadcast.enums.BroadcastPriority;
import com.edusync.api.course.broadcast.enums.BroadcastTarget;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Broadcasts", description = "Endpoints for managing broadcast announcements. Broadcasts allow sending messages to course participants with various targeting options.")
@RequestMapping("/broadcasts")
public interface BroadcastApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new broadcast",
            description = "Creates a new broadcast announcement. Supports targeting all course students, " +
                    "specific modules, specific sessions, specific students, or global broadcasts."
    )
    @ApiResponse(responseCode = "201", description = "Broadcast created successfully")
    @ApiResponse(responseCode = "404", description = "Course, module, sender, or student not found")
    BroadcastResponse create(@Valid @RequestBody BroadcastRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all broadcasts",
            description = "Returns all broadcasts. Supports filtering by course, target type, priority, " +
                    "and searching by title."
    )
    @ApiResponse(responseCode = "200", description = "Broadcasts retrieved successfully")
    List<BroadcastResponse> findAll(
            @RequestParam(required = false) UUID courseUuid,
            @RequestParam(required = false) BroadcastTarget targetType,
            @RequestParam(required = false) BroadcastPriority priority,
            @RequestParam(required = false) String search);

    @GetMapping("/{broadcastUuid}")
    @Operation(
            summary = "Get broadcast by UUID",
            description = "Retrieves a single broadcast message by its unique identifier."
    )
    @ApiResponse(responseCode = "200", description = "Broadcast retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Broadcast not found")
    BroadcastResponse findByUuid(@PathVariable UUID broadcastUuid);

    @GetMapping("/{broadcastUuid}/recipients")
    @Operation(
            summary = "List broadcast recipients",
            description = "Returns all recipients of a broadcast message, including read status and email delivery information."
    )
    @ApiResponse(responseCode = "200", description = "Recipients retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Broadcast not found")
    List<BroadcastRecipientResponse> findRecipients(@PathVariable UUID broadcastUuid);

    @PatchMapping("/{broadcastUuid}/recipients/{studentUuid}/read")
    @Operation(
            summary = "Mark broadcast as read",
            description = "Marks a broadcast as read for a specific student. Sets the read timestamp if not already read."
    )
    @ApiResponse(responseCode = "200", description = "Broadcast marked as read successfully")
    @ApiResponse(responseCode = "404", description = "Broadcast or recipient not found")
    BroadcastRecipientResponse markAsRead(@PathVariable UUID broadcastUuid, @PathVariable UUID studentUuid);
}
