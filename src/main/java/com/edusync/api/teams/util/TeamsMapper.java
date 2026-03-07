package com.edusync.api.teams.util;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.teams.dto.TeamsResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeamsMapper {

    public static TeamsResponse.SessionStudent mapStudentToSessionStudent(Student student) {
        return new TeamsResponse.SessionStudent(student.getUuid(), student.getStudentNumber(), student.getFirstName(), student.getLastName(), student.getEmail(), Objects.nonNull(student.getAzureAdGuestId()));
    }

    public static String formatAppUserFullName(AppUser user) {
        return "%s %s".formatted(user.getFirstName(), user.getLastName());
    }
}
