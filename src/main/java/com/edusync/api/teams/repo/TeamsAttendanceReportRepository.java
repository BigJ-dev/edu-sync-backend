package com.edusync.api.teams.repo;

import com.edusync.api.teams.enums.ReportSyncStatus;
import com.edusync.api.teams.model.TeamsAttendanceReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamsAttendanceReportRepository extends JpaRepository<TeamsAttendanceReport, Long> {

    Optional<TeamsAttendanceReport> findByUuid(UUID uuid);

    Optional<TeamsAttendanceReport> findByGraphReportId(String graphReportId);

    Optional<TeamsAttendanceReport> findByClassSessionId(Long classSessionId);

    List<TeamsAttendanceReport> findBySyncStatusOrderByCreatedAtAsc(ReportSyncStatus syncStatus);
}
