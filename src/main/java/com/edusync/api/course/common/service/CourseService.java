package com.edusync.api.course.common.service;

import com.edusync.api.course.common.dto.CourseRequest;
import com.edusync.api.course.common.dto.CourseResponse;
import com.edusync.api.course.common.enums.CourseStatus;
import com.edusync.api.course.common.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CourseService {

    CourseResponse createCourse(CourseRequest.Create request);

    Page<CourseResponse> findAllCourses(CourseStatus status, String search, Pageable pageable);

    CourseResponse findCourseByUuid(UUID uuid);

    CourseResponse updateCourse(UUID uuid, CourseRequest.Update request);

    CourseResponse updateCourseStatus(UUID uuid, CourseRequest.UpdateStatus request);

    Course findCourseEntityByUuid(UUID uuid);
}
