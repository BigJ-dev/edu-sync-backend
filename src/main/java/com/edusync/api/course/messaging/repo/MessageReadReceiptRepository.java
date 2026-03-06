package com.edusync.api.course.messaging.repo;

import com.edusync.api.course.messaging.model.MessageReadReceipt;
import com.edusync.api.course.messaging.model.MessageReadReceiptId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReadReceiptRepository extends JpaRepository<MessageReadReceipt, MessageReadReceiptId> {
}
