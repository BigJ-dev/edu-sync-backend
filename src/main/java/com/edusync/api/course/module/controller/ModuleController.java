package com.edusync.api.course.module.controller;

import com.edusync.api.course.module.controller.api.ModuleApi;
import com.edusync.api.course.module.dto.ModuleRequest;
import com.edusync.api.course.module.dto.ModuleResponse;
import com.edusync.api.course.module.enums.ModuleStatus;
import com.edusync.api.course.module.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class ModuleController implements ModuleApi {

    private final ModuleService service;

    @Override
    public ModuleResponse create(UUID courseUuid, ModuleRequest.Create request) {
        return service.createModule(courseUuid, request);
    }

    @Override
    public List<ModuleResponse> findAllByCourse(UUID courseUuid, ModuleStatus status, String search) {
        return service.findAllModulesByCourse(courseUuid, status, search);
    }

    @Override
    public ModuleResponse findByUuid(UUID courseUuid, UUID moduleUuid) {
        return service.findModuleByUuid(moduleUuid);
    }

    @Override
    public ModuleResponse update(UUID courseUuid, UUID moduleUuid, ModuleRequest.Update request) {
        return service.updateModule(moduleUuid, request);
    }

    @Override
    public ModuleResponse updateStatus(UUID courseUuid, UUID moduleUuid, ModuleRequest.UpdateStatus request) {
        return service.updateModuleStatus(moduleUuid, request);
    }
}
