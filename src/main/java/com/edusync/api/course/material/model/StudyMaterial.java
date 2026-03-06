package com.edusync.api.course.material.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.course.material.enums.MaterialType;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.session.model.ClassSession;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "study_material", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_session_id")
    private ClassSession classSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private AppUser uploadedBy;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "material_type")
    private MaterialType materialType;

    @Column(length = 500)
    private String s3Key;

    @Column(length = 1000)
    private String externalUrl;

    @Column(length = 255)
    private String fileName;

    private Long fileSizeBytes;

    @Column(length = 100)
    private String mimeType;

    @Column(nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean visibleToStudents;

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
