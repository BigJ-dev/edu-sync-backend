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
        return service.create(request);
    }

    @Override
    public List<GroupResponse> findAllByCourse(UUID courseUuid, String search) {
        return service.findAllByCourse(courseUuid, search);
    }

    @Override
    public GroupResponse findByUuid(UUID courseUuid, UUID groupUuid) {
        return service.findByUuid(groupUuid);
    }

    @Override
    public GroupResponse update(UUID courseUuid, UUID groupUuid, GroupRequest.Update request) {
        return service.update(groupUuid, request);
    }

    @Override
    public void delete(UUID courseUuid, UUID groupUuid) {
        service.delete(groupUuid);
    }

    @Override
    public GroupMemberResponse addMember(UUID courseUuid, UUID groupUuid, GroupRequest.AddMember request) {
        return service.addMember(groupUuid, request);
    }

    @Override
    public List<GroupMemberResponse> findMembers(UUID courseUuid, UUID groupUuid) {
        return service.findMembers(groupUuid);
    }

    @Override
    public void removeMember(UUID courseUuid, UUID groupUuid, UUID studentUuid) {
        service.removeMember(groupUuid, studentUuid);
    }

    @Override
    public GroupMemberResponse updateMemberRole(UUID courseUuid, UUID groupUuid, UUID studentUuid, GroupRequest.UpdateMemberRole request) {
        return service.updateMemberRole(groupUuid, studentUuid, request);
    }
}
