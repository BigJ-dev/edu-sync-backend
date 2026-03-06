package com.edusync.api.course.category.model;

import com.edusync.api.course.common.model.Course;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_category_mapping", schema = "edusync",
        uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "category_id"}))
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCategoryMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CourseCategory category;
}
