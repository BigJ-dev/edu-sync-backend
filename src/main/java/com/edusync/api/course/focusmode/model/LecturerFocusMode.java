package com.edusync.api.course.focusmode.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.module.model.CourseModule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "lecturer_focus_mode", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturerFocusMode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = false)
    private AppUser lecturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private CourseModule module;

    @Column(nullable = false)
    private boolean isActive;

    @Column(length = 255)
    private String reason;

    private Instant activatedAt;

    private Instant scheduledEnd;

    @UpdateTimestamp
    private Instant updatedAt;
}
