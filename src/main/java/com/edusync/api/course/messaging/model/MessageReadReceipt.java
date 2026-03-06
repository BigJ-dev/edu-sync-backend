package com.edusync.api.course.messaging.model;

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
public class MessageReadReceipt {

    @EmbeddedId
    private MessageReadReceiptId id;

    @Column(nullable = false)
    private Instant lastReadAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
