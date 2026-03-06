package com.edusync.api.course.group.dto;

import com.edusync.api.course.group.enums.GroupMemberRole;
import com.edusync.api.course.group.model.CourseGroupMember;

import java.time.Instant;
import java.util.UUID;

public record GroupMemberResponse(
        UUID groupUuid,
        UUID studentUuid,
        String studentName,
        GroupMemberRole role,
        Instant joinedAt
) {
    public static GroupMemberResponse from(CourseGroupMember member) {
        return new GroupMemberResponse(
                member.getGroup().getUuid(),
                member.getStudent().getUuid(),
                member.getStudent().getFirstName() + " " + member.getStudent().getLastName(),
                member.getRole(),
                member.getJoinedAt()
        );
    }
}
