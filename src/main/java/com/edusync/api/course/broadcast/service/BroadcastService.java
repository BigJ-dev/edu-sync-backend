package com.edusync.api.course.broadcast.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.broadcast.dto.BroadcastRecipientResponse;
import com.edusync.api.course.broadcast.dto.BroadcastRequest;
import com.edusync.api.course.broadcast.dto.BroadcastResponse;
import com.edusync.api.course.broadcast.enums.BroadcastPriority;
import com.edusync.api.course.broadcast.enums.BroadcastTarget;
import com.edusync.api.course.broadcast.model.BroadcastMessage;
import com.edusync.api.course.broadcast.model.BroadcastRecipient;
import com.edusync.api.course.broadcast.repo.BroadcastMessageRepository;
import com.edusync.api.course.broadcast.repo.BroadcastRecipientRepository;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
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
public class BroadcastService {

    private final BroadcastMessageRepository messageRepository;
    private final BroadcastRecipientRepository recipientRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;

    public BroadcastResponse create(BroadcastRequest.Create request) {
        var sender = findAppUser(request.sentByUuid());

        Course course = null;
        if (request.courseUuid() != null) {
            course = findCourse(request.courseUuid());
        }

        CourseModule module = null;
        if (request.moduleUuid() != null) {
            module = findModule(request.moduleUuid());
        }

        var message = BroadcastMessage.builder()
                .course(course)
                .module(module)
                .sentBy(sender)
                .title(request.title())
                .body(request.body())
                .targetType(request.targetType())
                .attachmentS3Key(request.attachmentS3Key())
                .attachmentName(request.attachmentName())
                .priority(request.priority())
                .sendEmail(request.sendEmail())
                .sentAt(Instant.now())
                .build();

        var savedMessage = messageRepository.save(message);

        if (request.studentUuids() != null && !request.studentUuids().isEmpty()) {
            for (UUID studentUuid : request.studentUuids()) {
                var student = findStudent(studentUuid);
                var recipient = BroadcastRecipient.builder()
                        .broadcastMessage(savedMessage)
                        .student(student)
                        .emailSent(false)
                        .build();
                recipientRepository.save(recipient);
            }
        }

        return BroadcastResponse.from(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<BroadcastResponse> findAll(UUID courseUuid, BroadcastTarget targetType, BroadcastPriority priority, String search) {
        Long courseId = null;
        if (courseUuid != null) {
            courseId = findCourse(courseUuid).getId();
        }

        var spec = Specification.where(BroadcastSpec.hasCourseId(courseId))
                .and(BroadcastSpec.hasTargetType(targetType))
                .and(BroadcastSpec.hasPriority(priority))
                .and(BroadcastSpec.searchByTitle(search));

        return messageRepository.findAll(spec).stream().map(BroadcastResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public BroadcastResponse findByUuid(UUID uuid) {
        return BroadcastResponse.from(findBroadcastMessage(uuid));
    }

    @Transactional(readOnly = true)
    public List<BroadcastRecipientResponse> findRecipients(UUID broadcastUuid) {
        var message = findBroadcastMessage(broadcastUuid);
        return recipientRepository.findByBroadcastMessageId(message.getId())
                .stream().map(BroadcastRecipientResponse::from).toList();
    }

    public BroadcastRecipientResponse markAsRead(UUID broadcastUuid, UUID studentUuid) {
        var message = findBroadcastMessage(broadcastUuid);
        var student = findStudent(studentUuid);

        var recipient = recipientRepository.findByBroadcastMessageIdAndStudentId(message.getId(), student.getId())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Broadcast recipient was not found"));

        if (recipient.getReadAt() == null) {
            recipient.setReadAt(Instant.now());
            recipientRepository.save(recipient);
        }

        return BroadcastRecipientResponse.from(recipient);
    }

    private BroadcastMessage findBroadcastMessage(UUID uuid) {
        return messageRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Broadcast message was not found"));
    }

    private Course findCourse(UUID uuid) {
        return courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
    }

    private CourseModule findModule(UUID uuid) {
        return moduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Module was not found"));
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }

    private Student findStudent(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }
}
