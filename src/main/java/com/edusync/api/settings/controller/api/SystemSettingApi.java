package com.edusync.api.settings.controller.api;

import com.edusync.api.settings.dto.SettingRequest;
import com.edusync.api.settings.dto.SettingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "System Settings", description = "Endpoints for managing system-wide configuration settings.")
@RequestMapping("/settings")
public interface SystemSettingApi {

    @GetMapping
    @Operation(
            summary = "List all system settings",
            description = "Returns all system settings, optionally filtered by category. " +
                    "Settings are ordered alphabetically by key."
    )
    @ApiResponse(responseCode = "200", description = "System settings retrieved successfully")
    List<SettingResponse> findAll(@RequestParam(required = false) String category);

    @GetMapping("/{key}")
    @Operation(
            summary = "Get system setting by key",
            description = "Retrieves a single system setting by its unique key."
    )
    @ApiResponse(responseCode = "200", description = "System setting retrieved successfully")
    @ApiResponse(responseCode = "404", description = "System setting not found")
    SettingResponse findByKey(@PathVariable String key);

    @PutMapping("/{key}")
    @Operation(
            summary = "Update a system setting",
            description = "Updates the value of an existing system setting and records who made the change."
    )
    @ApiResponse(responseCode = "200", description = "System setting updated successfully")
    @ApiResponse(responseCode = "404", description = "System setting or user not found")
    SettingResponse update(@PathVariable String key, @Valid @RequestBody SettingRequest.Update request);
}
