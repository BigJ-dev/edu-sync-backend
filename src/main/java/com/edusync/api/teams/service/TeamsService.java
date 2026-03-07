package com.edusync.api.teams.service;

import com.edusync.api.teams.dto.TeamsRequest;
import com.edusync.api.teams.dto.TeamsResponse;

import java.util.List;
import java.util.UUID;

public interface TeamsService {

    TeamsResponse.GuestInvite inviteStudentAsTeamsGuest(TeamsRequest.InviteGuest request);

    TeamsResponse.BulkInviteResult inviteStudentsForTeamsSession(TeamsRequest.BulkInvite request);

    TeamsResponse.MeetingCreated createTeamsOnlineClass(TeamsRequest.CreateMeeting request);

    List<TeamsResponse.SessionStudent> listStudentsForTeamsSession(UUID classSessionUuid);

    TeamsResponse.AttendanceSynced syncTeamsAttendance(TeamsRequest.SyncAttendance request);

    TeamsResponse.MonthlyReportResult generateTeamsMonthlyReport(TeamsRequest.MonthlyReport request);
}
