package com.edusync.api.teams.service;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.model.CourseEnrollment;
import com.edusync.api.course.enrollment.repo.EnrollmentRepository;
import com.edusync.api.course.group.model.CourseGroupMember;
import com.edusync.api.course.group.repo.CourseGroupMemberRepository;
import com.edusync.api.course.session.model.ClassSession;
import com.edusync.api.course.session.repo.ClassSessionRepository;
import com.edusync.api.course.attendance.repo.AttendanceRepository;
import com.edusync.api.teams.config.AzureAdProperties;
import com.edusync.api.teams.dto.AttendanceSyncContext;
import com.edusync.api.teams.dto.TeamsRequest;
import com.edusync.api.teams.dto.TeamsResponse;
import com.edusync.api.teams.enums.ReportSyncStatus;
import com.edusync.api.teams.model.TeamsAttendanceReport;
import com.edusync.api.teams.repo.TeamsAttendanceReportRepository;
import com.edusync.api.teams.util.TeamsAttendanceSyncUtil;
import com.edusync.api.teams.util.TeamsGraphUtil;
import com.edusync.api.teams.util.TeamsMapper;
import com.microsoft.graph.models.MeetingAttendanceReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    private final AzureAdProperties azureAdProperties;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassSessionRepository classSessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final TeamsAttendanceReportRepository teamsReportRepository;
    private final CourseGroupMemberRepository courseGroupMemberRepository;
    private final TeamsGraphUtil teamsGraphUtil;
    private final TeamsAttendanceSyncUtil attendanceSyncUtil;

    @Override
    public TeamsResponse.GuestInvite inviteStudentAsTeamsGuest(TeamsRequest.InviteGuest request) {
        var student = findStudentByUuid(request.studentUuid());
        validateNotAlreadyInvited(student);

        var invitation = teamsGraphUtil.postGuestInvitation(student);
        var guestId = extractGuestIdFromInvitation(invitation);

        Optional.ofNullable(guestId).ifPresent(id -> {
            student.setAzureAdGuestId(id);
            studentRepository.save(student);
        });

        return new TeamsResponse.GuestInvite(
                student.getUuid(), student.getEmail(),
                guestId, invitation.getInviteRedeemUrl()
        );
    }

    @Override
    public TeamsResponse.BulkInviteResult inviteStudentsForTeamsSession(TeamsRequest.BulkInvite request) {
        var session = findClassSessionByUuid(request.classSessionUuid());
        var students = resolveTargetStudents(session);

        var pendingInvitation = students.stream()
                .filter(student -> Objects.isNull(student.getAzureAdGuestId()))
                .toList();

        int skipped = students.size() - pendingInvitation.size();

        int invited = (int) pendingInvitation.stream()
                .filter(this::trySendGuestInvitation)
                .count();

        return new TeamsResponse.BulkInviteResult(
                session.getUuid(), students.size(), invited, skipped
        );
    }

    @Override
    public TeamsResponse.MeetingCreated createTeamsOnlineClass(TeamsRequest.CreateMeeting request) {
        var session = findClassSessionByUuid(request.classSessionUuid());
        validateNoExistingMeeting(session);

        var students = resolveTargetStudents(session);
        var meeting = buildOnlineMeetingForSession(session, students);
        var organizerId = azureAdProperties.getOrganizerUserId();
        var createdMeeting = teamsGraphUtil.postOnlineMeeting(organizerId, meeting);

        session.setTeamsMeetingId(createdMeeting.getId());
        session.setTeamsJoinUrl(createdMeeting.getJoinWebUrl());
        classSessionRepository.save(session);

        var lecturer = session.getLecturer();

        return new TeamsResponse.MeetingCreated(
                session.getUuid(), createdMeeting.getId(), createdMeeting.getJoinWebUrl(),
                lecturer.getUuid(), TeamsMapper.formatAppUserFullName(lecturer)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamsResponse.SessionStudent> listStudentsForTeamsSession(UUID classSessionUuid) {
        var session = findClassSessionByUuid(classSessionUuid);

        return resolveTargetStudents(session).stream()
                .map(TeamsMapper::mapStudentToSessionStudent)
                .toList();
    }

    @Override
    public TeamsResponse.AttendanceSynced syncTeamsAttendance(TeamsRequest.SyncAttendance request) {
        var session = findClassSessionByUuid(request.classSessionUuid());
        validateHasTeamsMeeting(session);

        var organizerId = azureAdProperties.getOrganizerUserId();
        var reports = teamsGraphUtil.fetchAttendanceReports(organizerId, session.getTeamsMeetingId());

        int totalSynced = reports.stream()
                .mapToInt(report -> processAttendanceReport(organizerId, session, report))
                .sum();

        return new TeamsResponse.AttendanceSynced(session.getUuid(), totalSynced);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamsResponse.MonthlyReportResult generateTeamsMonthlyReport(TeamsRequest.MonthlyReport request) {
        var course = findCourseByUuid(request.courseUuid());
        var dateRange = buildMonthlyDateRange(request.year(), request.month());

        var sessions = classSessionRepository
                .findByCourseIdAndScheduledStartBetween(course.getId(), dateRange.from(), dateRange.to());

        var sessionIds = sessions.stream().map(ClassSession::getId).toList();
        var allRecords = attendanceRepository.findByClassSessionIdIn(sessionIds);
        var enrollments = enrollmentRepository.findByCourseIdAndStatus(course.getId(), EnrollmentStatus.ENROLLED);

        var recordsByStudent = groupAttendanceRecordsByStudentId(allRecords);
        var recordsBySession = groupAttendanceRecordsBySessionId(allRecords);

        var studentSummaries = buildStudentAttendanceSummaries(enrollments, recordsByStudent, sessions.size());
        var classSummaries = buildClassAttendanceSummaries(sessions, recordsBySession, enrollments.size());

        return new TeamsResponse.MonthlyReportResult(
                course.getUuid(), request.year(), request.month(),
                studentSummaries, classSummaries
        );
    }

    private int processAttendanceReport(String organizerId, ClassSession session, MeetingAttendanceReport report) {
        var existingReport = teamsReportRepository.findByGraphReportId(report.getId());

        if (existingReport.isPresent() && existingReport.get().getSyncStatus() == ReportSyncStatus.COMPLETED) {
            return NO_RECORDS_SYNCED;
        }

        var graphRecords = teamsGraphUtil.fetchAttendanceRecords(organizerId, session.getTeamsMeetingId(), report.getId());
        if (graphRecords.isEmpty()) return NO_RECORDS_SYNCED;

        var teamsReport = getOrCreateTeamsReport(existingReport.orElse(null), session, report, graphRecords.size());
        var syncContext = new AttendanceSyncContext(session, teamsReport);

        int synced = (int) graphRecords.stream()
                .filter(graphRecord -> processStudentAttendance(syncContext, graphRecord))
                .count();

        markReportCompleted(teamsReport);
        return synced;
    }

    private boolean processStudentAttendance(
            AttendanceSyncContext context, com.microsoft.graph.models.AttendanceRecord graphRecord) {

        return Optional.ofNullable(graphRecord.getEmailAddress())
                .flatMap(studentRepository::findByEmail)
                .map(student -> {
                    int durationMinutes = extractAttendanceDurationMinutes(graphRecord);
                    var attendance = attendanceSyncUtil.upsertAttendanceRecord(context, student, durationMinutes);
                    attendance.ifPresent(record -> attendanceSyncUtil.saveAttendanceLogs(record, graphRecord));
                    return attendance.isPresent();
                })
                .orElse(false);
    }

    private TeamsAttendanceReport getOrCreateTeamsReport(
            TeamsAttendanceReport existing, ClassSession session,
            MeetingAttendanceReport report, int participantCount) {

        var teamsReport = Optional.ofNullable(existing).orElseGet(() ->
                TeamsAttendanceReport.builder()
                        .classSession(session)
                        .graphReportId(report.getId())
                        .rawJson(EMPTY_RAW_JSON)
                        .syncStatus(ReportSyncStatus.PROCESSING)
                        .retryCount(INITIAL_RETRY_COUNT)
                        .build()
        );

        Optional.ofNullable(report.getMeetingStartDateTime())
                .ifPresent(start -> teamsReport.setMeetingStart(start.toInstant()));

        Optional.ofNullable(report.getMeetingEndDateTime())
                .ifPresent(end -> teamsReport.setMeetingEnd(end.toInstant()));

        teamsReport.setTotalParticipantCount(participantCount);
        return teamsReportRepository.save(teamsReport);
    }

    private boolean trySendGuestInvitation(Student student) {
        try {
            var invitation = teamsGraphUtil.trySendGuestInvitation(student);
            if (Objects.isNull(invitation)) return false;

            Optional.ofNullable(extractGuestIdFromInvitation(invitation)).ifPresent(guestId -> {
                student.setAzureAdGuestId(guestId);
                studentRepository.save(student);
            });

            return true;
        } catch (Exception ex) {
            log.error("Failed to invite student {} as B2B guest: {}", student.getEmail(), ex.getMessage());
            return false;
        }
    }

    private List<Student> resolveTargetStudents(ClassSession session) {
        return Optional.ofNullable(session.getGroup())
                .map(group -> courseGroupMemberRepository.findByGroupId(group.getId()).stream()
                        .map(CourseGroupMember::getStudent)
                        .toList())
                .orElseGet(() -> enrollmentRepository
                        .findByCourseIdAndStatus(session.getModule().getCourse().getId(), EnrollmentStatus.ENROLLED)
                        .stream()
                        .map(CourseEnrollment::getStudent)
                        .toList());
    }

    private void validateNotAlreadyInvited(Student student) {
        if (Objects.nonNull(student.getAzureAdGuestId())) {
            throw new ServiceException(HttpStatus.CONFLICT,
                    "Student is already invited as a B2B guest (Azure AD ID: %s)".formatted(student.getAzureAdGuestId()));
        }
    }

    private void validateNoExistingMeeting(ClassSession session) {
        if (Objects.nonNull(session.getTeamsMeetingId())) {
            throw new ServiceException(HttpStatus.CONFLICT,
                    "A Teams meeting already exists for this class session");
        }
    }

    private void validateHasTeamsMeeting(ClassSession session) {
        if (Objects.isNull(session.getTeamsMeetingId())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "No Teams meeting associated with this class session");
        }
    }

    private void markReportCompleted(TeamsAttendanceReport report) {
        report.setSyncStatus(ReportSyncStatus.COMPLETED);
        report.setSyncedAt(Instant.now());
        teamsReportRepository.save(report);
    }

    private Student findStudentByUuid(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }

    private ClassSession findClassSessionByUuid(UUID uuid) {
        return classSessionRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Class session was not found"));
    }

    private Course findCourseByUuid(UUID uuid) {
        return courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
    }
}
