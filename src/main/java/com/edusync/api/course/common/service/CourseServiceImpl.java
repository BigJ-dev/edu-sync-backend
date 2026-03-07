package com.edusync.api.course.common.service;

import com.edusync.api.actor.common.enums.UserRole;
import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.dto.CourseRequest;
import com.edusync.api.course.common.dto.CourseResponse;
import com.edusync.api.course.common.enums.CourseStatus;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;
    private final AppUserRepository appUserRepository;

    @Override
    public CourseResponse createCourse(CourseRequest.Create request) {
        validateCodeUniqueness(request.code());
        var lecturer = findLecturer(request.lecturerUuid());

        var course = Course.builder()
                .code(request.code())
                .title(request.title())
                .description(request.description())
                .thumbnailS3Key(request.thumbnailS3Key())
                .lecturer(lecturer)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .minAttendancePct(request.minAttendancePct() != null ? request.minAttendancePct() : 80)
                .maxStudents(request.maxStudents())
                .status(CourseStatus.DRAFT)
                .build();

        return CourseResponse.from(repository.save(course));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> findAllCourses(CourseStatus status, String search, Pageable pageable) {
        var spec = Specification.where(CourseSpec.hasStatus(status))
                .and(CourseSpec.searchByCodeOrTitle(search));
        return repository.findAll(spec, pageable).map(CourseResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse findCourseByUuid(UUID uuid) {
        return CourseResponse.from(findCourseEntityByUuid(uuid));
    }

    @Override
    public CourseResponse updateCourse(UUID uuid, CourseRequest.Update request) {
        var course = findCourseEntityByUuid(uuid);
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setThumbnailS3Key(request.thumbnailS3Key());
        course.setStartDate(request.startDate());
        course.setEndDate(request.endDate());
        if (request.minAttendancePct() != null) course.setMinAttendancePct(request.minAttendancePct());
        course.setMaxStudents(request.maxStudents());
        return CourseResponse.from(repository.save(course));
    }

    @Override
    public CourseResponse updateCourseStatus(UUID uuid, CourseRequest.UpdateStatus request) {
        var course = findCourseEntityByUuid(uuid);
        course.setStatus(request.status());
        return CourseResponse.from(repository.save(course));
    }

    @Override
    public Course findCourseEntityByUuid(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
    }

    private void validateCodeUniqueness(String code) {
        if (repository.existsByCode(code))
            throw new ServiceException(HttpStatus.CONFLICT, "A course with this code already exists");
    }

    private AppUser findLecturer(UUID lecturerUuid) {
        return appUserRepository.findByUuidAndRole(lecturerUuid, UserRole.LECTURER)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Lecturer was not found"));
    }
}
