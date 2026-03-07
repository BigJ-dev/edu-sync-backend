package com.edusync.api.course.material.controller.api;

import com.edusync.api.course.material.dto.MaterialRequest;
import com.edusync.api.course.material.dto.MaterialResponse;
import com.edusync.api.course.material.enums.MaterialType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Study Materials", description = "Endpoints for managing study materials. Materials are learning resources (documents, videos, links, slides) attached to a module and optionally to a specific session.")
@RequestMapping("/modules/{moduleUuid}/materials")
public interface MaterialApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Upload a study material",
            description = "Creates a new study material within a module. Supports documents, videos, links, " +
                    "slide decks, and images. Can optionally be linked to a specific session."
    )
    @ApiResponse(responseCode = "201", description = "Material created successfully")
    @ApiResponse(responseCode = "404", description = "Module, session, or uploader not found")
    MaterialResponse createMaterial(@PathVariable UUID moduleUuid, @Valid @RequestBody MaterialRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all materials for a module",
            description = "Returns all study materials belonging to a module. Supports filtering by material type, " +
                    "visibility, and searching by title."
    )
    @ApiResponse(responseCode = "200", description = "Materials retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Module not found")
    List<MaterialResponse> findAllMaterialsByModule(
            @PathVariable UUID moduleUuid,
            @RequestParam(required = false) MaterialType type,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) String search);

    @GetMapping("/{materialUuid}")
    @Operation(
            summary = "Get material by UUID",
            description = "Retrieves a single study material's details by its unique identifier."
    )
    @ApiResponse(responseCode = "200", description = "Material retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Material not found")
    MaterialResponse findMaterialByUuid(@PathVariable UUID moduleUuid, @PathVariable UUID materialUuid);

    @PutMapping("/{materialUuid}")
    @Operation(
            summary = "Update material details",
            description = "Updates a study material's information such as title, description, file details, and visibility."
    )
    @ApiResponse(responseCode = "200", description = "Material updated successfully")
    @ApiResponse(responseCode = "404", description = "Material not found")
    MaterialResponse updateMaterial(@PathVariable UUID moduleUuid, @PathVariable UUID materialUuid, @Valid @RequestBody MaterialRequest.Update request);

    @DeleteMapping("/{materialUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a study material",
            description = "Permanently removes a study material from a module."
    )
    @ApiResponse(responseCode = "204", description = "Material deleted successfully")
    @ApiResponse(responseCode = "404", description = "Material not found")
    void deleteMaterial(@PathVariable UUID moduleUuid, @PathVariable UUID materialUuid);
}
