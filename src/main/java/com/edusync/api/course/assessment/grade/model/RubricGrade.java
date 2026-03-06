package com.edusync.api.course.assessment.grade.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.course.assessment.rubric.model.RubricCriteria;
import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "rubric_grade", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RubricGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private AssessmentSubmission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criteria_id", nullable = false)
    private RubricCriteria criteria;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal marksAwarded;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graded_by")
    private AppUser gradedBy;

    private Instant gradedAt;
}
