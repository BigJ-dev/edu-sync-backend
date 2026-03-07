package com.edusync.api.course.support.controller.api;

import com.edusync.api.course.support.dto.SupportMessageRequest;
import com.edusync.api.course.support.dto.SupportMessageResponse;
import com.edusync.api.course.support.enums.SenderType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Support Messages", description = "Endpoints for managing messages within a support thread.")
@RequestMapping("/support-threads/{threadUuid}/messages")
public interface SupportMessageApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Send a message",
            description = "Adds a new message to a support thread. Updates the thread's last message timestamp " +
                    "and increments the appropriate unread count."
    )
    @ApiResponse(responseCode = "201", description = "Message sent successfully")
    @ApiResponse(responseCode = "404", description = "Thread or sender not found")
    SupportMessageResponse create(@PathVariable UUID threadUuid, @Valid @RequestBody SupportMessageRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List messages in a thread",
            description = "Returns all messages in a support thread ordered by creation time ascending."
    )
    @ApiResponse(responseCode = "200", description = "Messages retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Thread not found")
    List<SupportMessageResponse> findByThread(@PathVariable UUID threadUuid);

    @PostMapping("/read")
    @Operation(
            summary = "Mark thread as read",
            description = "Marks all messages in a support thread as read for a given reader. " +
                    "Upserts a read receipt and resets the appropriate unread count to zero."
    )
    @ApiResponse(responseCode = "200", description = "Thread marked as read successfully")
    @ApiResponse(responseCode = "404", description = "Thread or reader not found")
    void markAsRead(@PathVariable UUID threadUuid, @RequestParam SenderType readerType, @RequestParam UUID readerUuid);
}
