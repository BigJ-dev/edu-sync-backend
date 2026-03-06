package com.edusync.api.course.group.model;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.course.group.enums.GroupMemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "course_group_member", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseGroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private CourseGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "group_member_role")
    private GroupMemberRole role;

    @Column(nullable = false)
    private Instant joinedAt;

    @PrePersist
    void setDefaults() {
        if (joinedAt == null) joinedAt = Instant.now();
    }
}
