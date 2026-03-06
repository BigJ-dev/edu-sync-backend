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
    public MaterialResponse create(UUID moduleUuid, MaterialRequest.Create request) {
        return service.create(moduleUuid, request);
    }

    @Override
    public List<MaterialResponse> findAllByModule(UUID moduleUuid, MaterialType type, Boolean visible, String search) {
        return service.findAllByModule(moduleUuid, type, visible, search);
    }

    @Override
    public MaterialResponse findByUuid(UUID moduleUuid, UUID materialUuid) {
        return service.findByUuid(materialUuid);
    }

    @Override
    public MaterialResponse update(UUID moduleUuid, UUID materialUuid, MaterialRequest.Update request) {
        return service.update(materialUuid, request);
    }

    @Override
    public void delete(UUID moduleUuid, UUID materialUuid) {
        service.delete(materialUuid);
    }
}
