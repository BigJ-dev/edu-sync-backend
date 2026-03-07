package com.edusync.api.course.material.controller;

import com.edusync.api.course.material.controller.api.MaterialApi;
import com.edusync.api.course.material.dto.MaterialRequest;
import com.edusync.api.course.material.dto.MaterialResponse;
import com.edusync.api.course.material.enums.MaterialType;
import com.edusync.api.course.material.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class MaterialController implements MaterialApi {

    private final MaterialService service;

    @Override
    public MaterialResponse createMaterial(UUID moduleUuid, MaterialRequest.Create request) {
        return service.createMaterial(moduleUuid, request);
    }

    @Override
    public List<MaterialResponse> findAllMaterialsByModule(UUID moduleUuid, MaterialType type, Boolean visible, String search) {
        return service.findAllMaterialsByModule(moduleUuid, type, visible, search);
    }

    @Override
    public MaterialResponse findMaterialByUuid(UUID moduleUuid, UUID materialUuid) {
        return service.findMaterialByUuid(materialUuid);
    }

    @Override
    public MaterialResponse updateMaterial(UUID moduleUuid, UUID materialUuid, MaterialRequest.Update request) {
        return service.updateMaterial(materialUuid, request);
    }

    @Override
    public void deleteMaterial(UUID moduleUuid, UUID materialUuid) {
        service.deleteMaterial(materialUuid);
    }
}
