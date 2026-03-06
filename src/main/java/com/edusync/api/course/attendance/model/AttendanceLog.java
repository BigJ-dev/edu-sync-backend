package com.edusync.api.course.attendance.model;

import com.edusync.api.course.attendance.enums.AttendanceEvent;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "attendance_log", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_record_id", nullable = false)
    private AttendanceRecord attendanceRecord;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "attendance_event")
    private AttendanceEvent eventType;

    @Column(nullable = false)
    private Instant eventTime;

    @Column(length = 255)
    private String teamsIdentityEmail;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;
}
