package com.edusync.api.course.support.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.support.enums.ThreadPriority;
import com.edusync.api.course.support.enums.ThreadStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "message_thread", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, length = 255)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "thread_status")
    private ThreadStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "thread_priority")
    private ThreadPriority priority;

    @Column(nullable = false)
    private boolean isEscalated;

    private Instant escalatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escalated_by")
    private AppUser escalatedBy;

    private Instant lastMessageAt;

    @Column(nullable = false)
    private int studentUnreadCount;

    @Column(nullable = false)
    private int staffUnreadCount;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    void generateUuid() {
        if (uuid == null) uuid = UUID.randomUUID();
    }
}
