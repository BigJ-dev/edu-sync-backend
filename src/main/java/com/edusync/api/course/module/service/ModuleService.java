package com.edusync.api.course.module.service;

import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.service.CourseService;
import com.edusync.api.course.module.dto.ModuleRequest;
import com.edusync.api.course.module.dto.ModuleResponse;
import com.edusync.api.course.module.enums.ModuleStatus;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.module.repo.ModuleRepository;
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
public class ModuleService {

    private final ModuleRepository repository;
    private final CourseService courseService;

    public ModuleResponse create(UUID courseUuid, ModuleRequest.Create request) {
        var course = courseService.findCourse(courseUuid);
        validateSortOrderUniqueness(course.getId(), request.sortOrder());

        var module = CourseModule.builder()
                .course(course)
                .title(request.title())
                .description(request.description())
                .sortOrder(request.sortOrder())
                .status(ModuleStatus.DRAFT)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        return ModuleResponse.from(repository.save(module));
    }

    @Transactional(readOnly = true)
    public List<ModuleResponse> findAllByCourse(UUID courseUuid, ModuleStatus status, String search) {
        var course = courseService.findCourse(courseUuid);
        var spec = Specification.where(ModuleSpec.hasCourseId(course.getId()))
                .and(ModuleSpec.hasStatus(status))
                .and(ModuleSpec.searchByTitle(search));
        return repository.findAll(spec).stream().map(ModuleResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ModuleResponse findByUuid(UUID moduleUuid) {
        return ModuleResponse.from(findModule(moduleUuid));
    }

    public ModuleResponse update(UUID moduleUuid, ModuleRequest.Update request) {
        var module = findModule(moduleUuid);

        if (module.getSortOrder() != request.sortOrder()) {
            validateSortOrderUniqueness(module.getCourse().getId(), request.sortOrder());
        }

        module.setTitle(request.title());
        module.setDescription(request.description());
        module.setSortOrder(request.sortOrder());
        module.setStartDate(request.startDate());
        module.setEndDate(request.endDate());
        return ModuleResponse.from(repository.save(module));
    }

    public ModuleResponse updateStatus(UUID moduleUuid, ModuleRequest.UpdateStatus request) {
        var module = findModule(moduleUuid);
        module.setStatus(request.status());
        return ModuleResponse.from(repository.save(module));
    }

    private CourseModule findModule(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Module was not found"));
    }

    private void validateSortOrderUniqueness(Long courseId, int sortOrder) {
        if (repository.existsByCourseIdAndSortOrder(courseId, sortOrder))
            throw new ServiceException(HttpStatus.CONFLICT, "A module with this sort order already exists in the course");
    }
}
