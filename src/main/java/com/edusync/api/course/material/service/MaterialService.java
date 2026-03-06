package com.edusync.api.course.material.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.material.dto.MaterialRequest;
import com.edusync.api.course.material.dto.MaterialResponse;
import com.edusync.api.course.material.enums.MaterialType;
import com.edusync.api.course.material.model.StudyMaterial;
import com.edusync.api.course.material.repo.MaterialRepository;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.module.repo.ModuleRepository;
import com.edusync.api.course.session.model.ClassSession;
import com.edusync.api.course.session.repo.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialService {

    private final MaterialRepository repository;
    private final ModuleRepository moduleRepository;
    private final ClassSessionRepository classSessionRepository;
    private final AppUserRepository appUserRepository;

    public MaterialResponse create(UUID moduleUuid, MaterialRequest.Create request) {
        var module = findModule(moduleUuid);
        var uploadedBy = findAppUser(request.uploadedByUuid());
        ClassSession session = null;
        if (request.classSessionUuid() != null) {
            session = classSessionRepository.findByUuid(request.classSessionUuid())
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Class session was not found"));
        }

        var material = StudyMaterial.builder()
                .module(module)
                .classSession(session)
                .uploadedBy(uploadedBy)
                .title(request.title())
                .description(request.description())
                .materialType(request.materialType())
                .s3Key(request.s3Key())
                .externalUrl(request.externalUrl())
                .fileName(request.fileName())
                .fileSizeBytes(request.fileSizeBytes())
                .mimeType(request.mimeType())
                .sortOrder(request.sortOrder() != null ? request.sortOrder() : 0)
                .visibleToStudents(request.visibleToStudents() != null ? request.visibleToStudents() : true)
                .build();

        return MaterialResponse.from(repository.save(material));
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> findAllByModule(UUID moduleUuid, MaterialType type, Boolean visible, String search) {
        var module = findModule(moduleUuid);
        var spec = Specification.where(MaterialSpec.hasModuleId(module.getId()))
                .and(MaterialSpec.hasMaterialType(type))
                .and(MaterialSpec.isVisibleToStudents(visible))
                .and(MaterialSpec.searchByTitle(search));
        return repository.findAll(spec).stream().map(MaterialResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public MaterialResponse findByUuid(UUID materialUuid) {
        return MaterialResponse.from(findMaterial(materialUuid));
    }

    public MaterialResponse update(UUID materialUuid, MaterialRequest.Update request) {
        var material = findMaterial(materialUuid);
        material.setTitle(request.title());
        material.setDescription(request.description());
        material.setS3Key(request.s3Key());
        material.setExternalUrl(request.externalUrl());
        material.setFileName(request.fileName());
        material.setFileSizeBytes(request.fileSizeBytes());
        material.setMimeType(request.mimeType());
        if (request.sortOrder() != null) material.setSortOrder(request.sortOrder());
        if (request.visibleToStudents() != null) material.setVisibleToStudents(request.visibleToStudents());
        return MaterialResponse.from(repository.save(material));
    }

    public void delete(UUID materialUuid) {
        var material = findMaterial(materialUuid);
        repository.delete(material);
    }

    private StudyMaterial findMaterial(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Study material was not found"));
    }

    private CourseModule findModule(UUID uuid) {
        return moduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Module was not found"));
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }
}
