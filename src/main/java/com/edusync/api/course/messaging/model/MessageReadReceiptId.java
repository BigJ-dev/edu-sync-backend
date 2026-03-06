package com.edusync.api.course.messaging.model;

import com.edusync.api.course.messaging.enums.SenderType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MessageReadReceiptId implements Serializable {

    @Column(name = "thread_id", nullable = false)
    private Long threadId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reader_type", nullable = false, columnDefinition = "sender_type")
    private SenderType readerType;

    @Column(name = "reader_id", nullable = false)
    private Long readerId;
}
