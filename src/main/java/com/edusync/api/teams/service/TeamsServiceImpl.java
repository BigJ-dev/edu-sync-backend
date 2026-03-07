package com.edusync.api.teams.service;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.attendance.enums.AttendanceEvent;
import com.edusync.api.course.attendance.model.AttendanceRecord;
import com.edusync.api.course.attendance.repo.AttendanceLogRepository;
import com.edusync.api.course.attendance.repo.AttendanceRepository;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.model.CourseEnrollment;
import com.edusync.api.course.enrollment.repo.EnrollmentRepository;
import com.edusync.api.course.group.model.CourseGroupMember;
import com.edusync.api.course.group.repo.CourseGroupMemberRepository;
import com.edusync.api.course.session.model.ClassSession;
import com.edusync.api.course.session.repo.ClassSessionRepository;
import com.edusync.api.teams.config.AzureAdProperties;
import com.edusync.api.teams.dto.TeamsRequest;
import com.edusync.api.teams.dto.TeamsResponse;
import com.edusync.api.teams.enums.ReportSyncStatus;
import com.edusync.api.teams.model.TeamsAttendanceReport;
import com.edusync.api.teams.repo.TeamsAttendanceReportRepository;
import com.edusync.api.teams.util.TeamsMapper;
import com.microsoft.graph.models.Invitation;
import com.microsoft.graph.models.MeetingAttendanceReport;
import com.microsoft.graph.models.OnlineMeeting;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.edusync.api.teams.util.TeamsAttendanceCalculator.*;
import static com.edusync.api.teams.util.TeamsConstants.*;
import static com.edusync.api.teams.util.TeamsInvitationBuilder.*;
import static com.edusync.api.teams.util.TeamsMeetingBuilder.*;
import static com.edusync.api.teams.util.TeamsReportBuilder.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@ConditionalOnProperty(prefix = "azure.ad", name = {"tenant-id", "client-id", "client-secret"})
public class TeamsServiceImpl implements TeamsService {

    private final GraphServiceClient graphClient;
    private final AzureAdProperties azureAdProperties;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassSessionRepository classSessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceLogRepository attendanceLogRepository;
    private final TeamsAttendanceReportRepository teamsReportRepository;
    private final CourseGroupMemberRepository courseGroupMemberRepository;

    @Override
    public TeamsResponse.GuestInvite inviteStudentAsTeamsGuest(TeamsRequest.InviteGuest request) {
        var student = findStudentOrThrow(request.studentUuid());
        validateNotAlreadyInvited(student);

        var result = postInvitationOrThrow(student);
        var guestId = extractGuestIdFromInvitation(result);

        if (Objects.nonNull(guestId)) {
            student.setAzureAdGuestId(guestId);
            studentRepository.save(student);
        }

        return new TeamsResponse.GuestInvite(student.getUuid(), student.getEmail(), guestId, result.getInviteRedeemUrl());
    }

    @Override
    public TeamsResponse.BulkInviteResult inviteStudentsForTeamsSession(TeamsRequest.BulkInvite request) {
        var session = findClassSessionOrThrow(request.classSessionUuid());
        var students = resolveTargetStudents(session);

        var alreadyInvited = students.stream().filter(s -> Objects.nonNull(s.getAzureAdGuestId())).toList();
        var pendingInvitation = students.stream().filter(s -> Objects.isNull(s.getAzureAdGuestId())).toList();

        int skipped = alreadyInvited.size();
        int invited = (int) pendingInvitation.stream().filter(this::trySendGuestInvitation).count();

        return new TeamsResponse.BulkInviteResult(session.getUuid(), students.size(), invited, skipped);
    }

    @Override
    public TeamsResponse.MeetingCreated createTeamsOnlineClass(TeamsRequest.CreateMeeting request) {
        var session = findClassSessionOrThrow(request.classSessionUuid());
        validateNoExistingMeeting(session);

        var students = resolveTargetStudents(session);
        var meeting = buildOnlineMeetingForSession(session, students);

        var result = postOnlineMeetingOrThrow(meeting);

        session.setTeamsMeetingId(result.getId());
        session.setTeamsJoinUrl(result.getJoinWebUrl());
        classSessionRepository.save(session);

        var lecturer = session.getLecturer();
        return new TeamsResponse.MeetingCreated(session.getUuid(), result.getId(), result.getJoinWebUrl(), lecturer.getUuid(), TeamsMapper.formatAppUserFullName(lecturer));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamsResponse.SessionStudent> listStudentsForTeamsSession(UUID classSessionUuid) {
        var session = findClassSessionOrThrow(classSessionUuid);

        return resolveTargetStudents(session).stream()
                .map(TeamsMapper::mapStudentToSessionStudent)
                .toList();
    }

    @Override
    public TeamsResponse.AttendanceSynced syncTeamsAttendance(TeamsRequest.SyncAttendance request) {
        var session = findClassSessionOrThrow(request.classSessionUuid());
        validateHasTeamsMeeting(session);

        var organizerId = azureAdProperties.getOrganizerUserId();
        var reports = fetchAttendanceReports(organizerId, session.getTeamsMeetingId());

        int totalSynced = reports.stream()
                .mapToInt(report -> processAttendanceReport(organizerId, session, report))
                .sum();

        return new TeamsResponse.AttendanceSynced(session.getUuid(), totalSynced);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamsResponse.MonthlyReportResult generateTeamsMonthlyReport(TeamsRequest.MonthlyReport request) {
        var course = findCourseOrThrow(request.courseUuid());
        var dateRange = buildMonthlyDateRange(request.year(), request.month());

        var sessions = classSessionRepository.findByCourseIdAndScheduledStartBetween(course.getId(), dateRange.from(), dateRange.to());
        var sessionIds = sessions.stream().map(ClassSession::getId).toList();
        var allRecords = attendanceRepository.findByClassSessionIdIn(sessionIds);

        var enrollments = enrollmentRepository.findByCourseIdAndStatus(course.getId(), EnrollmentStatus.ENROLLED);

        var recordsByStudent = groupAttendanceRecordsByStudentId(allRecords);
        var recordsBySession = groupAttendanceRecordsBySessionId(allRecords);

        var studentSummaries = buildStudentAttendanceSummaries(enrollments, recordsByStudent, sessions.size());
        var classSummaries = buildClassAttendanceSummaries(sessions, recordsBySession, enrollments.size());

        return new TeamsResponse.MonthlyReportResult(course.getUuid(), request.year(), request.month(), studentSummaries, classSummaries);
    }

    private Student findStudentOrThrow(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }

    private ClassSession findClassSessionOrThrow(UUID uuid) {
        return classSessionRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Class session was not found"));
    }

    private Course findCourseOrThrow(UUID uuid) {
        return courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
    }

    private List<Student> resolveTargetStudents(ClassSession session) {
        if (Objects.nonNull(session.getGroup())) {
            return courseGroupMemberRepository.findByGroupId(session.getGroup().getId())
                    .stream()
                    .map(CourseGroupMember::getStudent)
                    .toList();
        }
        return enrollmentRepository
                .findByCourseIdAndStatus(session.getModule().getCourse().getId(), EnrollmentStatus.ENROLLED)
                .stream()
                .map(CourseEnrollment::getStudent)
                .toList();
    }

    private void validateNotAlreadyInvited(Student student) {
        if (Objects.nonNull(student.getAzureAdGuestId())) {
            throw new ServiceException(HttpStatus.CONFLICT, "Student is already invited as a B2B guest (Azure AD ID: %s)".formatted(student.getAzureAdGuestId()));
        }
    }

    private void validateNoExistingMeeting(ClassSession session) {
        if (Objects.nonNull(session.getTeamsMeetingId())) {
            throw new ServiceException(HttpStatus.CONFLICT, "A Teams meeting already exists for this class session");
        }
    }

    private void validateHasTeamsMeeting(ClassSession session) {
        if (Objects.isNull(session.getTeamsMeetingId())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "No Teams meeting associated with this class session");
        }
    }

    private Invitation postInvitationOrThrow(Student student) {
        var result = graphClient.invitations().post(buildGuestInvitation(student));
        if (Objects.isNull(result)) {
            throw new ServiceException(HttpStatus.BAD_GATEWAY, "Failed to send guest invitation — no response from Microsoft Graph");
        }
        return result;
    }

    private OnlineMeeting postOnlineMeetingOrThrow(OnlineMeeting meeting) {
        var result = graphClient.users().byUserId(azureAdProperties.getOrganizerUserId()).onlineMeetings().post(meeting);
        if (Objects.isNull(result)) {
            throw new ServiceException(HttpStatus.BAD_GATEWAY, "Failed to create Teams meeting — no response from Microsoft Graph");
        }
        return result;
    }

    private boolean trySendGuestInvitation(Student student) {
        try {
            var result = graphClient.invitations().post(buildGuestInvitation(student));
            if (Objects.isNull(result)) return false;

            var guestId = extractGuestIdFromInvitation(result);
            if (Objects.nonNull(guestId)) {
                student.setAzureAdGuestId(guestId);
                studentRepository.save(student);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to invite student {} as B2B guest: {}", student.getEmail(), e.getMessage());
            return false;
        }
    }

    private List<MeetingAttendanceReport> fetchAttendanceReports(String organizerId, String meetingId) {
        var reportsPage = graphClient.users().byUserId(organizerId).onlineMeetings().byOnlineMeetingId(meetingId).attendanceReports().get();
        if (Objects.isNull(reportsPage) || Objects.isNull(reportsPage.getValue()) || reportsPage.getValue().isEmpty()) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "No attendance reports found for this meeting");
        }
        return reportsPage.getValue();
    }

    private List<com.microsoft.graph.models.AttendanceRecord> fetchAttendanceRecords(String organizerId, String meetingId, String reportId) {
        var recordsPage = graphClient.users().byUserId(organizerId).onlineMeetings().byOnlineMeetingId(meetingId).attendanceReports().byMeetingAttendanceReportId(reportId).attendanceRecords().get();
        if (Objects.isNull(recordsPage) || Objects.isNull(recordsPage.getValue())) {
            return Collections.emptyList();
        }
        return recordsPage.getValue();
    }

    private int processAttendanceReport(String organizerId, ClassSession session, MeetingAttendanceReport report) {
        var existingReport = teamsReportRepository.findByGraphReportId(report.getId());
        if (existingReport.isPresent() && existingReport.get().getSyncStatus() == ReportSyncStatus.COMPLETED) return NO_RECORDS_SYNCED;

        var graphRecords = fetchAttendanceRecords(organizerId, session.getTeamsMeetingId(), report.getId());
        if (graphRecords.isEmpty()) return NO_RECORDS_SYNCED;

        var teamsReport = getOrCreateReport(existingReport.orElse(null), session, report, graphRecords.size());

        int synced = (int) graphRecords.stream().filter(graphRecord -> processStudentAttendance(session, teamsReport, graphRecord)).count();
        markReportCompleted(teamsReport);
        return synced;
    }

    private TeamsAttendanceReport getOrCreateReport(TeamsAttendanceReport existing, ClassSession session, MeetingAttendanceReport report, int participantCount) {
        var teamsReport = Objects.nonNull(existing) ? existing : TeamsAttendanceReport.builder()
                .classSession(session).graphReportId(report.getId()).rawJson(EMPTY_RAW_JSON).syncStatus(ReportSyncStatus.PROCESSING).retryCount(INITIAL_RETRY_COUNT).build();

        if (Objects.nonNull(report.getMeetingStartDateTime())) {
            teamsReport.setMeetingStart(report.getMeetingStartDateTime().toInstant());
        }
        if (Objects.nonNull(report.getMeetingEndDateTime())) {
            teamsReport.setMeetingEnd(report.getMeetingEndDateTime().toInstant());
        }
        teamsReport.setTotalParticipantCount(participantCount);

        return teamsReportRepository.save(teamsReport);
    }

    private boolean processStudentAttendance(ClassSession session, TeamsAttendanceReport teamsReport, com.microsoft.graph.models.AttendanceRecord graphRecord) {

        if (Objects.isNull(graphRecord.getEmailAddress())) return false;

        var student = studentRepository.findByEmail(graphRecord.getEmailAddress()).orElse(null);
        if (Objects.isNull(student)) return false;

        int durationMinutes = extractAttendanceDurationMinutes(graphRecord);
        var attendance = upsertAttendanceRecord(session, student, teamsReport, durationMinutes);
        if (Objects.isNull(attendance)) return false;

        saveAttendanceLogs(attendance, graphRecord);
        return true;
    }

    private AttendanceRecord upsertAttendanceRecord(ClassSession session, Student student, TeamsAttendanceReport teamsReport, int durationMinutes) {
        var existing = attendanceRepository.findByClassSessionIdAndStudentId(session.getId(), student.getId());

        if (existing.isPresent()) {
            var record = existing.get();
            if (!record.isSyncedFromTeams() && record.isManuallyOverridden()) {
                return null;
            }
            record.setTotalDurationMinutes(durationMinutes);
            record.setAttendanceStatus(calculateAttendanceStatusFromDuration(durationMinutes, session));
            record.setSyncedFromTeams(true);
            record.setReportId(teamsReport.getId());
            return attendanceRepository.save(record);
        }

        var record = AttendanceRecord.builder()
                .classSession(session)
                .student(student)
                .course(session.getModule().getCourse())
                .module(session.getModule())
                .totalDurationMinutes(durationMinutes)
                .attendanceStatus(calculateAttendanceStatusFromDuration(durationMinutes, session))
                .syncedFromTeams(true)
                .manuallyOverridden(false)
                .reportId(teamsReport.getId())
                .build();
        return attendanceRepository.save(record);
    }

    private void saveAttendanceLogs(AttendanceRecord attendance, com.microsoft.graph.models.AttendanceRecord graphRecord) {
        if (Objects.isNull(graphRecord.getAttendanceIntervals())) return;

        for (var interval : graphRecord.getAttendanceIntervals()) {
            if (Objects.nonNull(interval.getJoinDateTime())) {
                attendanceLogRepository.save(buildTeamsAttendanceLog(attendance, AttendanceEvent.JOIN, interval.getJoinDateTime().toInstant(), graphRecord.getEmailAddress()));
            }
            if (Objects.nonNull(interval.getLeaveDateTime())) {
                attendanceLogRepository.save(buildTeamsAttendanceLog(attendance, AttendanceEvent.LEAVE, interval.getLeaveDateTime().toInstant(), graphRecord.getEmailAddress()));
            }
        }
    }

    private void markReportCompleted(TeamsAttendanceReport report) {
        report.setSyncStatus(ReportSyncStatus.COMPLETED);
        report.setSyncedAt(Instant.now());
        teamsReportRepository.save(report);
    }
}
