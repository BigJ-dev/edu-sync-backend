package com.edusync.api.course.attendance.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.session.model.ClassSession;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "attendance_record", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_session_id", nullable = false)
    private ClassSession classSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule module;

    private Instant joinTime;

    private Instant leaveTime;

    private Integer totalDurationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "attendance_status")
    private AttendanceStatus attendanceStatus;

    private Long reportId;

    @Column(nullable = false)
    private boolean syncedFromTeams;

    @Column(nullable = false)
    private boolean manuallyOverridden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "override_by")
    private AppUser overrideBy;

    @Column(length = 500)
    private String overrideReason;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
