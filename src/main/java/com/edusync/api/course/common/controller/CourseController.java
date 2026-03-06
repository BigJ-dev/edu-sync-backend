package com.edusync.api.course.common.controller;

import com.edusync.api.course.common.controller.api.CourseApi;
import com.edusync.api.course.common.dto.CourseRequest;
import com.edusync.api.course.common.dto.CourseResponse;
import com.edusync.api.course.common.enums.CourseStatus;
import com.edusync.api.course.common.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class CourseController implements CourseApi {

    private final CourseService service;

    @Override
    public CourseResponse create(CourseRequest.Create request) {
        return service.create(request);
    }

    @Override
    public Page<CourseResponse> findAll(CourseStatus status, String search, Pageable pageable) {
        return service.findAll(status, search, pageable);
    }

    @Override
    public CourseResponse findByUuid(UUID uuid) {
        return service.findByUuid(uuid);
    }

    @Override
    public CourseResponse update(UUID uuid, CourseRequest.Update request) {
        return service.update(uuid, request);
    }

    @Override
    public CourseResponse updateStatus(UUID uuid, CourseRequest.UpdateStatus request) {
        return service.updateStatus(uuid, request);
    }
}
