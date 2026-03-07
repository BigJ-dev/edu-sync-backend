package com.edusync.api.course.support.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "message_read_receipt", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportReadReceipt {

    @EmbeddedId
    private SupportReadReceiptId id;

    @Column(nullable = false)
    private Instant lastReadAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
