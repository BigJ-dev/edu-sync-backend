package com.edusync.api.course.group.controller;

import com.edusync.api.course.group.controller.api.CourseGroupApi;
import com.edusync.api.course.group.dto.GroupMemberResponse;
import com.edusync.api.course.group.dto.GroupRequest;
import com.edusync.api.course.group.dto.GroupResponse;
import com.edusync.api.course.group.service.CourseGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class CourseGroupController implements CourseGroupApi {

    private final CourseGroupService service;

    @Override
    public GroupResponse create(UUID courseUuid, GroupRequest.Create request) {
        return service.createCourseGroup(request);
    }

    @Override
    public List<GroupResponse> findAllByCourse(UUID courseUuid, String search) {
        return service.findAllCourseGroupsByCourse(courseUuid, search);
    }

    @Override
    public GroupResponse findByUuid(UUID courseUuid, UUID groupUuid) {
        return service.findCourseGroupByUuid(groupUuid);
    }

    @Override
    public GroupResponse update(UUID courseUuid, UUID groupUuid, GroupRequest.Update request) {
        return service.updateCourseGroup(groupUuid, request);
    }

    @Override
    public void delete(UUID courseUuid, UUID groupUuid) {
        service.deleteCourseGroup(groupUuid);
    }

    @Override
    public GroupMemberResponse addMember(UUID courseUuid, UUID groupUuid, GroupRequest.AddMember request) {
        return service.addMemberToCourseGroup(groupUuid, request);
    }

    @Override
    public List<GroupMemberResponse> findMembers(UUID courseUuid, UUID groupUuid) {
        return service.findCourseGroupMembers(groupUuid);
    }

    @Override
    public void removeMember(UUID courseUuid, UUID groupUuid, UUID studentUuid) {
        service.removeMemberFromCourseGroup(groupUuid, studentUuid);
    }

    @Override
    public GroupMemberResponse updateMemberRole(UUID courseUuid, UUID groupUuid, UUID studentUuid, GroupRequest.UpdateMemberRole request) {
        return service.updateCourseGroupMemberRole(groupUuid, studentUuid, request);
    }
}
