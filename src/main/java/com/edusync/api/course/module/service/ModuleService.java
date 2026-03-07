package com.edusync.api.course.module.service;

import com.edusync.api.course.module.dto.ModuleRequest;
import com.edusync.api.course.module.dto.ModuleResponse;
import com.edusync.api.course.module.enums.ModuleStatus;

import java.util.List;
import java.util.UUID;

public interface ModuleService {

    ModuleResponse createModule(UUID courseUuid, ModuleRequest.Create request);

    List<ModuleResponse> findAllModulesByCourse(UUID courseUuid, ModuleStatus status, String search);

    ModuleResponse findModuleByUuid(UUID moduleUuid);

    ModuleResponse updateModule(UUID moduleUuid, ModuleRequest.Update request);

    ModuleResponse updateModuleStatus(UUID moduleUuid, ModuleRequest.UpdateStatus request);
}
