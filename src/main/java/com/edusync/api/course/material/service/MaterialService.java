package com.edusync.api.course.material.service;

import com.edusync.api.course.material.dto.MaterialRequest;
import com.edusync.api.course.material.dto.MaterialResponse;
import com.edusync.api.course.material.enums.MaterialType;

import java.util.List;
import java.util.UUID;

public interface MaterialService {

    MaterialResponse createMaterial(UUID moduleUuid, MaterialRequest.Create request);

    List<MaterialResponse> findAllMaterialsByModule(UUID moduleUuid, MaterialType type, Boolean visible, String search);

    MaterialResponse findMaterialByUuid(UUID materialUuid);

    MaterialResponse updateMaterial(UUID materialUuid, MaterialRequest.Update request);

    void deleteMaterial(UUID materialUuid);
}
