package com.edusync.api.course.session.service;

import com.edusync.api.actor.common.enums.UserRole;
import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.session.dto.ClassSessionRequest;
import com.edusync.api.course.session.dto.ClassSessionResponse;
import com.edusync.api.course.session.enums.ClassSessionStatus;
import com.edusync.api.course.session.enums.ClassSessionType;
import com.edusync.api.course.session.model.ClassSession;
import com.edusync.api.course.session.repo.ClassSessionRepository;
import com.edusync.api.course.group.model.CourseGroup;
import com.edusync.api.course.group.repo.CourseGroupRepository;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.module.repo.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassSessionServiceImpl implements ClassSessionService {

    private final ClassSessionRepository repository;
    private final ModuleRepository moduleRepository;
    private final AppUserRepository appUserRepository;
    private final CourseGroupRepository courseGroupRepository;

    @Override
    public ClassSessionResponse createClassSession(UUID moduleUuid, ClassSessionRequest.Create request) {
        var module = findModule(moduleUuid);
        var lecturer = findLecturer(request.lecturerUuid());
        validateSessionNumberUniqueness(module.getId(), request.sessionNumber());

        CourseGroup group = request.groupUuid() != null ? findGroup(request.groupUuid()) : null;

        var session = ClassSession.builder()
                .module(module)
                .lecturer(lecturer)
                .group(group)
                .title(request.title())
                .description(request.description())
                .sessionType(request.sessionType())
                .sessionNumber(request.sessionNumber())
                .scheduledStart(request.scheduledStart())
                .scheduledEnd(request.scheduledEnd())
                .teamsMeetingId(request.teamsMeetingId())
                .teamsJoinUrl(request.teamsJoinUrl())
                .venue(request.venue())
                .status(ClassSessionStatus.SCHEDULED)
                .build();

        return ClassSessionResponse.from(repository.save(session));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassSessionResponse> findAllClassSessionsByModule(UUID moduleUuid, ClassSessionStatus status, ClassSessionType sessionType, String search) {
        var module = findModule(moduleUuid);
        var spec = Specification.where(ClassSessionSpec.hasModuleId(module.getId()))
                .and(ClassSessionSpec.hasStatus(status))
                .and(ClassSessionSpec.hasSessionType(sessionType))
                .and(ClassSessionSpec.searchByTitle(search));
        return repository.findAll(spec).stream().map(ClassSessionResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClassSessionResponse findClassSessionByUuid(UUID sessionUuid) {
        return ClassSessionResponse.from(findClassSessionEntityByUuid(sessionUuid));
    }

    @Override
    public ClassSessionResponse updateClassSession(UUID sessionUuid, ClassSessionRequest.Update request) {
        var session = findClassSessionEntityByUuid(sessionUuid);

        if (session.getSessionNumber() != request.sessionNumber()) {
            validateSessionNumberUniqueness(session.getModule().getId(), request.sessionNumber());
        }

        session.setTitle(request.title());
        session.setDescription(request.description());
        session.setSessionNumber(request.sessionNumber());
        session.setScheduledStart(request.scheduledStart());
        session.setScheduledEnd(request.scheduledEnd());
        session.setGroup(request.groupUuid() != null ? findGroup(request.groupUuid()) : null);
        session.setTeamsMeetingId(request.teamsMeetingId());
        session.setTeamsJoinUrl(request.teamsJoinUrl());
        session.setVenue(request.venue());
        return ClassSessionResponse.from(repository.save(session));
    }

    @Override
    public ClassSessionResponse updateClassSessionStatus(UUID sessionUuid, ClassSessionRequest.UpdateStatus request) {
        var session = findClassSessionEntityByUuid(sessionUuid);

        if (request.status() == ClassSessionStatus.LIVE && session.getActualStart() == null) {
            session.setActualStart(Instant.now());
        }
        if (request.status() == ClassSessionStatus.COMPLETED && session.getActualEnd() == null) {
            session.setActualEnd(Instant.now());
        }

        session.setStatus(request.status());
        return ClassSessionResponse.from(repository.save(session));
    }

    @Override
    public ClassSession findClassSessionEntityByUuid(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Class session was not found"));
    }

    private CourseModule findModule(UUID uuid) {
        return moduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Module was not found"));
    }

    private AppUser findLecturer(UUID lecturerUuid) {
        return appUserRepository.findByUuidAndRole(lecturerUuid, UserRole.LECTURER)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Lecturer was not found"));
    }

    private CourseGroup findGroup(UUID uuid) {
        return courseGroupRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course group was not found"));
    }

    private void validateSessionNumberUniqueness(Long moduleId, int sessionNumber) {
        if (repository.existsByModuleIdAndSessionNumber(moduleId, sessionNumber))
            throw new ServiceException(HttpStatus.CONFLICT, "A session with this number already exists in the module");
    }
}
