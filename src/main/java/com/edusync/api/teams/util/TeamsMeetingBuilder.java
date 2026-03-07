package com.edusync.api.teams.util;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.course.session.model.ClassSession;
import com.microsoft.graph.models.Identity;
import com.microsoft.graph.models.IdentitySet;
import com.microsoft.graph.models.LobbyBypassScope;
import com.microsoft.graph.models.LobbyBypassSettings;
import com.microsoft.graph.models.MeetingParticipantInfo;
import com.microsoft.graph.models.MeetingParticipants;
import com.microsoft.graph.models.OnlineMeeting;
import com.microsoft.graph.models.OnlineMeetingPresenters;
import com.microsoft.graph.models.OnlineMeetingRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeamsMeetingBuilder {

    public static OnlineMeeting buildOnlineMeetingForSession(ClassSession session, List<Student> students) {
        var meeting = new OnlineMeeting();
        meeting.setSubject(buildMeetingSubjectFromSession(session));
        meeting.setStartDateTime(convertInstantToOffsetDateTime(session.getScheduledStart()));
        meeting.setEndDateTime(convertInstantToOffsetDateTime(session.getScheduledEnd()));

        var lobbySettings = new LobbyBypassSettings();
        lobbySettings.setScope(LobbyBypassScope.Invited);
        meeting.setLobbyBypassSettings(lobbySettings);
        meeting.setAllowedPresenters(OnlineMeetingPresenters.Organizer);

        meeting.setParticipants(buildMeetingParticipants(session.getLecturer(), students));

        return meeting;
    }

    public static String buildMeetingSubjectFromSession(ClassSession session) {
        var subject = session.getTitle();
        if (Objects.nonNull(session.getGroup())) {
            subject += " (%s)".formatted(session.getGroup().getName());
        }
        return subject;
    }

    public static MeetingParticipants buildMeetingParticipants(AppUser lecturer, List<Student> students) {
        var participants = new MeetingParticipants();
        participants.setOrganizer(buildOrganizerParticipantInfo(lecturer.getEmail()));

        var attendees = students.stream().map(s -> buildAttendeeParticipantInfo(s.getEmail())).collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        participants.setAttendees(attendees);

        return participants;
    }

    public static MeetingParticipantInfo buildOrganizerParticipantInfo(String email) {
        var info = new MeetingParticipantInfo();
        info.setIdentity(buildUserIdentitySet(email));
        return info;
    }

    public static MeetingParticipantInfo buildAttendeeParticipantInfo(String email) {
        var info = buildOrganizerParticipantInfo(email);
        info.setRole(OnlineMeetingRole.Attendee);
        return info;
    }

    public static IdentitySet buildUserIdentitySet(String email) {
        var user = new Identity();
        user.setId(email);
        var identitySet = new IdentitySet();
        identitySet.setUser(user);
        return identitySet;
    }

    public static OffsetDateTime convertInstantToOffsetDateTime(Instant instant) {
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
