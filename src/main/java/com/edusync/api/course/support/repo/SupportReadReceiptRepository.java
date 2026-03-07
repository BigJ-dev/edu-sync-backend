package com.edusync.api.course.support.repo;

import com.edusync.api.course.support.model.SupportReadReceipt;
import com.edusync.api.course.support.model.SupportReadReceiptId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportReadReceiptRepository extends JpaRepository<SupportReadReceipt, SupportReadReceiptId> {
}
