package com.edusync.api.teams.controller.api;

import com.edusync.api.teams.dto.TeamsRequest;
import com.edusync.api.teams.dto.TeamsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Microsoft Teams", description = "Endpoints for Microsoft Teams B2B integration: guest invitations, meeting creation, attendance sync, and monthly reporting.")
@RequestMapping("/teams")
public interface TeamsApi {

    @PostMapping("/invite-guest")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Invite student as Azure AD B2B guest",
            description = "Sends a B2B guest invitation to the student's email via Microsoft Graph API. " +
                    "The student receives a one-time email to accept the invitation. " +
                    "Stores the Azure AD guest ID on the student record."
    )
    @ApiResponse(responseCode = "201", description = "Guest invitation sent successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    @ApiResponse(responseCode = "409", description = "Student is already invited as a B2B guest")
    TeamsResponse.GuestInvite inviteStudentAsTeamsGuest(@Valid @RequestBody TeamsRequest.InviteGuest request);

    @PostMapping("/create-meeting")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create Teams online meeting for a class session",
            description = "Creates a Microsoft Teams online meeting for the specified class session. " +
                    "Automatically adds all enrolled students as attendees with lobby bypass. " +
                    "Sets lobby to invited-users-only and allows only organizer as presenter."
    )
    @ApiResponse(responseCode = "201", description = "Teams meeting created successfully")
    @ApiResponse(responseCode = "404", description = "Class session not found")
    @ApiResponse(responseCode = "409", description = "Meeting already exists for this class session")
    TeamsResponse.MeetingCreated createTeamsOnlineClass(@Valid @RequestBody TeamsRequest.CreateMeeting request);

    @PostMapping("/bulk-invite")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Bulk invite students as Azure AD B2B guests",
            description = "Invites all students for a class session as B2B guests. " +
                    "If the session is linked to a group, only group members are invited. " +
                    "Otherwise all enrolled students are invited. Skips already-invited students."
    )
    @ApiResponse(responseCode = "201", description = "Bulk invite completed")
    @ApiResponse(responseCode = "404", description = "Class session not found")
    TeamsResponse.BulkInviteResult inviteStudentsForTeamsSession(@Valid @RequestBody TeamsRequest.BulkInvite request);

    @GetMapping("/sessions/{classSessionUuid}/students")
    @Operation(
            summary = "List students for a class session",
            description = "Returns the students targeted for a class session (group members if a group is assigned, " +
                    "otherwise all enrolled students). Shows each student's Azure AD invite status."
    )
    @ApiResponse(responseCode = "200", description = "Students listed successfully")
    @ApiResponse(responseCode = "404", description = "Class session not found")
    List<TeamsResponse.SessionStudent> listStudentsForTeamsSession(@PathVariable UUID classSessionUuid);

    @PostMapping("/sync-attendance")
    @Operation(
            summary = "Sync attendance from Teams meeting",
            description = "Fetches attendance reports from Microsoft Graph API for the class session's Teams meeting. " +
                    "Creates or updates attendance records and logs join/leave events. " +
                    "Skips manually overridden records."
    )
    @ApiResponse(responseCode = "200", description = "Attendance synced successfully")
    @ApiResponse(responseCode = "400", description = "No Teams meeting associated with this session")
    @ApiResponse(responseCode = "404", description = "Class session not found or no attendance reports")
    TeamsResponse.AttendanceSynced syncTeamsAttendance(@Valid @RequestBody TeamsRequest.SyncAttendance request);

    @PostMapping("/monthly-report")
    @Operation(
            summary = "Generate monthly attendance report",
            description = "Generates a month-end attendance report for a course. " +
                    "Includes per-student summaries (classes scheduled vs attended, total minutes) " +
                    "and per-class summaries (attendance percentage, average duration)."
    )
    @ApiResponse(responseCode = "200", description = "Monthly report generated successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    TeamsResponse.MonthlyReportResult generateTeamsMonthlyReport(@Valid @RequestBody TeamsRequest.MonthlyReport request);
}
