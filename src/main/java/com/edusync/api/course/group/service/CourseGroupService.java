package com.edusync.api.course.group.service;

import com.edusync.api.course.group.dto.GroupMemberResponse;
import com.edusync.api.course.group.dto.GroupRequest;
import com.edusync.api.course.group.dto.GroupResponse;

import java.util.List;
import java.util.UUID;

public interface CourseGroupService {

    GroupResponse createCourseGroup(GroupRequest.Create request);

    List<GroupResponse> findAllCourseGroupsByCourse(UUID courseUuid, String search);

    GroupResponse findCourseGroupByUuid(UUID groupUuid);

    GroupResponse updateCourseGroup(UUID groupUuid, GroupRequest.Update request);

    void deleteCourseGroup(UUID groupUuid);

    GroupMemberResponse addMemberToCourseGroup(UUID groupUuid, GroupRequest.AddMember request);

    void removeMemberFromCourseGroup(UUID groupUuid, UUID studentUuid);

    GroupMemberResponse updateCourseGroupMemberRole(UUID groupUuid, UUID studentUuid, GroupRequest.UpdateMemberRole request);

    List<GroupMemberResponse> findCourseGroupMembers(UUID groupUuid);
}
