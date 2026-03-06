package com.edusync.api.course.category.service;

import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.category.dto.CategoryRequest;
import com.edusync.api.course.category.dto.CategoryResponse;
import com.edusync.api.course.category.model.CourseCategory;
import com.edusync.api.course.category.model.CourseCategoryMapping;
import com.edusync.api.course.category.repo.CourseCategoryMappingRepository;
import com.edusync.api.course.category.repo.CourseCategoryRepository;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
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
public class CourseCategoryService {

    private final CourseCategoryRepository repository;
    private final CourseCategoryMappingRepository mappingRepository;
    private final CourseRepository courseRepository;

    public CategoryResponse create(CategoryRequest.Create request) {
        CourseCategory parent = null;
        Long parentId = null;

        if (request.parentUuid() != null) {
            parent = findCategoryEntity(request.parentUuid());
            parentId = parent.getId();
        }

        validateUniqueName(request.name(), parentId);

        var category = CourseCategory.builder()
                .name(request.name())
                .description(request.description())
                .parent(parent)
                .sortOrder(request.sortOrder())
                .iconName(request.iconName())
                .active(true)
                .build();

        return CategoryResponse.from(repository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll(Boolean active, String search) {
        var spec = Specification.where(CategorySpec.isActive(active))
                .and(CategorySpec.searchByName(search));
        return repository.findAll(spec).stream().map(CategoryResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findRoots() {
        return repository.findByParentIdIsNullOrderBySortOrderAsc()
                .stream().map(CategoryResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findChildren(UUID parentUuid) {
        var parent = findCategoryEntity(parentUuid);
        return repository.findByParentIdOrderBySortOrderAsc(parent.getId())
                .stream().map(CategoryResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findByUuid(UUID categoryUuid) {
        return CategoryResponse.from(findCategoryEntity(categoryUuid));
    }

    public CategoryResponse update(UUID categoryUuid, CategoryRequest.Update request) {
        var category = findCategoryEntity(categoryUuid);

        Long parentId = category.getParent() != null ? category.getParent().getId() : null;
        if (!category.getName().equals(request.name())) {
            validateUniqueName(request.name(), parentId);
        }

        category.setName(request.name());
        category.setDescription(request.description());
        category.setSortOrder(request.sortOrder());
        category.setIconName(request.iconName());
        category.setActive(request.active());

        return CategoryResponse.from(repository.save(category));
    }

    public void delete(UUID categoryUuid) {
        var category = findCategoryEntity(categoryUuid);
        repository.delete(category);
    }

    public CategoryResponse assignCourse(UUID categoryUuid, CategoryRequest.AssignCourse request) {
        var category = findCategoryEntity(categoryUuid);
        var course = findCourse(request.courseUuid());

        if (mappingRepository.existsByCourseIdAndCategoryId(course.getId(), category.getId())) {
            throw new ServiceException(HttpStatus.CONFLICT, "Course is already assigned to this category");
        }

        var mapping = CourseCategoryMapping.builder()
                .course(course)
                .category(category)
                .build();

        mappingRepository.save(mapping);
        return CategoryResponse.from(category);
    }

    public void unassignCourse(UUID categoryUuid, UUID courseUuid) {
        var category = findCategoryEntity(categoryUuid);
        var course = findCourse(courseUuid);

        if (!mappingRepository.existsByCourseIdAndCategoryId(course.getId(), category.getId())) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Course is not assigned to this category");
        }

        mappingRepository.deleteByCourseIdAndCategoryId(course.getId(), category.getId());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findCourseCategories(UUID courseUuid) {
        var course = findCourse(courseUuid);
        return mappingRepository.findByCourseId(course.getId())
                .stream()
                .map(mapping -> CategoryResponse.from(mapping.getCategory()))
                .toList();
    }

    private CourseCategory findCategoryEntity(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course category was not found"));
    }

    private Course findCourse(UUID uuid) {
        return courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
    }

    private void validateUniqueName(String name, Long parentId) {
        if (repository.existsByNameAndParentId(name, parentId)) {
            throw new ServiceException(HttpStatus.CONFLICT, "A category with this name already exists under the same parent");
        }
    }
}
