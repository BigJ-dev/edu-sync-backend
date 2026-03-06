package com.edusync.api.course.group.repo;

import com.edusync.api.course.group.model.CourseGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseGroupMemberRepository extends JpaRepository<CourseGroupMember, Long> {

    List<CourseGroupMember> findByGroupId(Long groupId);

    Optional<CourseGroupMember> findByGroupIdAndStudentId(Long groupId, Long studentId);

    boolean existsByGroupIdAndStudentId(Long groupId, Long studentId);

    long countByGroupId(Long groupId);
}
