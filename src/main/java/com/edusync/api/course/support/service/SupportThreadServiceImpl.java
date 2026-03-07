package com.edusync.api.course.support.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.support.dto.SupportThreadRequest;
import com.edusync.api.course.support.dto.SupportThreadResponse;
import com.edusync.api.course.support.enums.SenderType;
import com.edusync.api.course.support.enums.ThreadPriority;
import com.edusync.api.course.support.enums.ThreadStatus;
import com.edusync.api.course.support.model.SupportMessage;
import com.edusync.api.course.support.model.SupportThread;
import com.edusync.api.course.support.repo.SupportMessageRepository;
import com.edusync.api.course.support.repo.SupportThreadRepository;
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
public class SupportThreadServiceImpl implements SupportThreadService {

    private final SupportThreadRepository threadRepository;
    private final SupportMessageRepository messageRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public SupportThreadResponse createThread(SupportThreadRequest.Create request) {
        var course = findCourse(request.courseUuid());
        var student = findStudent(request.studentUuid());

        var priority = request.priority() != null ? request.priority() : ThreadPriority.NORMAL;
        var now = Instant.now();

        var thread = SupportThread.builder()
                .course(course)
                .student(student)
                .subject(request.subject())
                .status(ThreadStatus.OPEN)
                .priority(priority)
                .isEscalated(false)
                .lastMessageAt(now)
                .studentUnreadCount(0)
                .staffUnreadCount(1)
                .build();

        thread = threadRepository.save(thread);

        var message = SupportMessage.builder()
                .thread(thread)
                .senderType(SenderType.STUDENT)
                .senderId(student.getId())
                .body(request.body())
                .isSystemMessage(false)
                .build();

        messageRepository.save(message);

        return SupportThreadResponse.from(thread);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportThreadResponse> findAllThreads(UUID courseUuid, ThreadStatus status, ThreadPriority priority, Boolean escalated, String search) {
        var course = findCourse(courseUuid);
        var spec = Specification.where(SupportThreadSpec.hasCourseId(course.getId()))
                .and(SupportThreadSpec.hasStatus(status))
                .and(SupportThreadSpec.hasPriority(priority))
                .and(SupportThreadSpec.isEscalated(escalated))
                .and(SupportThreadSpec.searchBySubject(search));
        return threadRepository.findAll(spec).stream().map(SupportThreadResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SupportThreadResponse findThreadByUuid(UUID threadUuid) {
        return SupportThreadResponse.from(findThreadEntityByUuid(threadUuid));
    }

    @Override
    public SupportThreadResponse updateThreadStatus(UUID threadUuid, SupportThreadRequest.UpdateStatus request) {
        var thread = findThreadEntityByUuid(threadUuid);
        thread.setStatus(request.status());
        return SupportThreadResponse.from(threadRepository.save(thread));
    }

    @Override
    public SupportThreadResponse escalateThread(UUID threadUuid, UUID escalatedByUuid) {
        var thread = findThreadEntityByUuid(threadUuid);
        var escalatedBy = findAppUser(escalatedByUuid);

        thread.setEscalated(true);
        thread.setEscalatedAt(Instant.now());
        thread.setEscalatedBy(escalatedBy);

        return SupportThreadResponse.from(threadRepository.save(thread));
    }

    @Override
    public SupportThread findThreadEntityByUuid(UUID uuid) {
        return threadRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Support thread was not found"));
    }

    private Course findCourse(UUID uuid) {
        return courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
    }

    private Student findStudent(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }
}
