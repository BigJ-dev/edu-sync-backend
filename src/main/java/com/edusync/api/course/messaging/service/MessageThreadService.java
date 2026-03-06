package com.edusync.api.course.messaging.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.messaging.dto.ThreadRequest;
import com.edusync.api.course.messaging.dto.ThreadResponse;
import com.edusync.api.course.messaging.enums.SenderType;
import com.edusync.api.course.messaging.enums.ThreadPriority;
import com.edusync.api.course.messaging.enums.ThreadStatus;
import com.edusync.api.course.messaging.model.Message;
import com.edusync.api.course.messaging.model.MessageThread;
import com.edusync.api.course.messaging.repo.MessageRepository;
import com.edusync.api.course.messaging.repo.MessageThreadRepository;
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
public class MessageThreadService {

    private final MessageThreadRepository threadRepository;
    private final MessageRepository messageRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;

    public ThreadResponse createThread(ThreadRequest.Create request) {
        var course = findCourse(request.courseUuid());
        var student = findStudent(request.studentUuid());

        var priority = request.priority() != null ? request.priority() : ThreadPriority.NORMAL;
        var now = Instant.now();

        var thread = MessageThread.builder()
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

        var message = Message.builder()
                .thread(thread)
                .senderType(SenderType.STUDENT)
                .senderId(student.getId())
                .body(request.body())
                .isSystemMessage(false)
                .build();

        messageRepository.save(message);

        return ThreadResponse.from(thread);
    }

    @Transactional(readOnly = true)
    public List<ThreadResponse> findAll(UUID courseUuid, ThreadStatus status, ThreadPriority priority, Boolean escalated, String search) {
        var course = findCourse(courseUuid);
        var spec = Specification.where(ThreadSpec.hasCourseId(course.getId()))
                .and(ThreadSpec.hasStatus(status))
                .and(ThreadSpec.hasPriority(priority))
                .and(ThreadSpec.isEscalated(escalated))
                .and(ThreadSpec.searchBySubject(search));
        return threadRepository.findAll(spec).stream().map(ThreadResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ThreadResponse findByUuid(UUID threadUuid) {
        return ThreadResponse.from(findThread(threadUuid));
    }

    public ThreadResponse updateStatus(UUID threadUuid, ThreadRequest.UpdateStatus request) {
        var thread = findThread(threadUuid);
        thread.setStatus(request.status());
        return ThreadResponse.from(threadRepository.save(thread));
    }

    public ThreadResponse escalate(UUID threadUuid, UUID escalatedByUuid) {
        var thread = findThread(threadUuid);
        var escalatedBy = findAppUser(escalatedByUuid);

        thread.setEscalated(true);
        thread.setEscalatedAt(Instant.now());
        thread.setEscalatedBy(escalatedBy);

        return ThreadResponse.from(threadRepository.save(thread));
    }

    public MessageThread findThread(UUID uuid) {
        return threadRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Message thread was not found"));
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
