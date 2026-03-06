package com.edusync.api.course.focusmode.repo;

import com.edusync.api.course.focusmode.model.LecturerFocusMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FocusModeRepository extends JpaRepository<LecturerFocusMode, Long> {

    Optional<LecturerFocusMode> findByLecturerIdAndCourseId(Long lecturerId, Long courseId);

    Optional<LecturerFocusMode> findByLecturerIdAndCourseIdAndModuleId(Long lecturerId, Long courseId, Long moduleId);

    List<LecturerFocusMode> findByLecturerId(Long lecturerId);

    List<LecturerFocusMode> findByLecturerIdAndIsActiveTrue(Long lecturerId);

    List<LecturerFocusMode> findByCourseIdAndIsActiveTrue(Long courseId);
}
