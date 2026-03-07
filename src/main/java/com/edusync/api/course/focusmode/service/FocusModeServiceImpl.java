package com.edusync.api.course.focusmode.service;

import com.edusync.api.actor.common.enums.UserRole;
import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.focusmode.dto.FocusModeRequest;
import com.edusync.api.course.focusmode.dto.FocusModeResponse;
import com.edusync.api.course.focusmode.model.LecturerFocusMode;
import com.edusync.api.course.focusmode.repo.FocusModeRepository;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.module.repo.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FocusModeServiceImpl implements FocusModeService {

    private final FocusModeRepository repository;
    private final AppUserRepository appUserRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    @Override
    public FocusModeResponse activateFocusMode(FocusModeRequest.Activate request) {
        var lecturer = findLecturer(request.lecturerUuid());
        var course = findCourse(request.courseUuid());

        var module = Objects.nonNull(request.moduleUuid())
                ? findModule(request.moduleUuid())
                : null;

        if (Objects.nonNull(request.scheduledEnd()) && Instant.now().isAfter(request.scheduledEnd())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Scheduled end must be in the future");
        }

        var focusMode = findOrCreate(lecturer, course, module);
        focusMode.setActive(true);
        focusMode.setActivatedAt(Instant.now());
        focusMode.setReason(request.reason());
        focusMode.setScheduledEnd(request.scheduledEnd());

        return FocusModeResponse.from(repository.save(focusMode));
    }

    @Override
    public FocusModeResponse deactivateFocusMode(UUID lecturerUuid, UUID courseUuid, UUID moduleUuid) {
        var lecturer = findLecturer(lecturerUuid);
        var course = findCourse(courseUuid);

        var focusMode = Objects.nonNull(moduleUuid)
                ? repository.findByLecturerIdAndCourseIdAndModuleId(lecturer.getId(), course.getId(), findModule(moduleUuid).getId())
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Focus mode record was not found"))
                : repository.findByLecturerIdAndCourseId(lecturer.getId(), course.getId())
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Focus mode record was not found"));

        focusMode.setActive(false);
        focusMode.setActivatedAt(null);
        focusMode.setScheduledEnd(null);

        return FocusModeResponse.from(repository.save(focusMode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FocusModeResponse> findFocusModesByLecturer(UUID lecturerUuid) {
        var lecturer = findLecturer(lecturerUuid);
        return repository.findByLecturerId(lecturer.getId()).stream()
                .map(FocusModeResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FocusModeResponse> findActiveFocusModesByCourse(UUID courseUuid) {
        var course = findCourse(courseUuid);
        return repository.findByCourseIdAndIsActiveTrue(course.getId()).stream()
                .map(FocusModeResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFocusModeActive(UUID lecturerUuid, UUID courseUuid, UUID moduleUuid) {
        var lecturer = findLecturer(lecturerUuid);
        var course = findCourse(courseUuid);

        if (Objects.nonNull(moduleUuid)) {
            var module = findModule(moduleUuid);
            return repository.findByLecturerIdAndCourseIdAndModuleId(lecturer.getId(), course.getId(), module.getId())
                    .map(LecturerFocusMode::isActive)
                    .orElse(false);
        }

        return repository.findByLecturerIdAndCourseId(lecturer.getId(), course.getId())
                .map(LecturerFocusMode::isActive)
                .orElse(false);
    }

    private LecturerFocusMode findOrCreate(AppUser lecturer, Course course, CourseModule module) {
        if (Objects.nonNull(module)) {
            return repository.findByLecturerIdAndCourseIdAndModuleId(lecturer.getId(), course.getId(), module.getId())
                    .orElseGet(() -> LecturerFocusMode.builder()
                            .lecturer(lecturer)
                            .course(course)
                            .module(module)
                            .isActive(false)
                            .build());
        }
        return repository.findByLecturerIdAndCourseId(lecturer.getId(), course.getId())
                .orElseGet(() -> LecturerFocusMode.builder()
                        .lecturer(lecturer)
                        .course(course)
                        .isActive(false)
                        .build());
    }

    private AppUser findLecturer(UUID lecturerUuid) {
        return appUserRepository.findByUuidAndRole(lecturerUuid, UserRole.LECTURER)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Lecturer was not found"));
    }

    private Course findCourse(UUID courseUuid) {
        return courseRepository.findByUuid(courseUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
    }

    private CourseModule findModule(UUID moduleUuid) {
        return moduleRepository.findByUuid(moduleUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Module was not found"));
    }
}
