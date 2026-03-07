package com.edusync.api.teams.util;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.common.exception.ServiceException;
import com.microsoft.graph.models.*;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.edusync.api.teams.util.TeamsInvitationBuilder.buildGuestInvitation;

@Component
@RequiredArgsConstructor
public class TeamsGraphUtil {

    private final GraphServiceClient graphClient;

    public Invitation postGuestInvitation(Student student) {
        return Optional.ofNullable(graphClient.invitations().post(buildGuestInvitation(student)))
                .orElseThrow(() -> new ServiceException(HttpStatus.BAD_GATEWAY,
                        "Failed to send guest invitation — no response from Microsoft Graph"));
    }

    public Invitation trySendGuestInvitation(Student student) {
        return graphClient.invitations().post(buildGuestInvitation(student));
    }

    public OnlineMeeting postOnlineMeeting(String organizerId, OnlineMeeting meeting) {
        return Optional.ofNullable(graphClient.users().byUserId(organizerId).onlineMeetings().post(meeting))
                .orElseThrow(() -> new ServiceException(HttpStatus.BAD_GATEWAY,
                        "Failed to create Teams meeting — no response from Microsoft Graph"));
    }

    public List<MeetingAttendanceReport> fetchAttendanceReports(String organizerId, String meetingId) {
        var reportsPage = graphClient.users().byUserId(organizerId)
                .onlineMeetings().byOnlineMeetingId(meetingId)
                .attendanceReports().get();

        return Optional.ofNullable(reportsPage)
                .map(MeetingAttendanceReportCollectionResponse::getValue)
                .filter(reports -> !reports.isEmpty())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND,
                        "No attendance reports found for this meeting"));
    }

    public List<com.microsoft.graph.models.AttendanceRecord> fetchAttendanceRecords(
            String organizerId, String meetingId, String reportId) {

        var recordsPage = graphClient.users().byUserId(organizerId)
                .onlineMeetings().byOnlineMeetingId(meetingId)
                .attendanceReports().byMeetingAttendanceReportId(reportId)
                .attendanceRecords().get();

        return Optional.ofNullable(recordsPage)
                .map(AttendanceRecordCollectionResponse::getValue)
                .orElse(Collections.emptyList());
    }
}
