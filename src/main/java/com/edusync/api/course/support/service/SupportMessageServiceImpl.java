package com.edusync.api.course.support.service;

import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.support.dto.SupportMessageRequest;
import com.edusync.api.course.support.dto.SupportMessageResponse;
import com.edusync.api.course.support.enums.SenderType;
import com.edusync.api.course.support.model.SupportMessage;
import com.edusync.api.course.support.model.SupportReadReceipt;
import com.edusync.api.course.support.model.SupportReadReceiptId;
import com.edusync.api.course.support.repo.SupportMessageRepository;
import com.edusync.api.course.support.repo.SupportReadReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SupportMessageServiceImpl implements SupportMessageService {

    private final SupportMessageRepository messageRepository;
    private final SupportReadReceiptRepository readReceiptRepository;
    private final SupportThreadService threadService;
    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public SupportMessageResponse addMessage(UUID threadUuid, SupportMessageRequest.Create request) {
        var thread = threadService.findThreadEntityByUuid(threadUuid);
        var senderId = resolveSenderId(request.senderType(), request.senderUuid());

        var message = SupportMessage.builder()
                .thread(thread)
                .senderType(request.senderType())
                .senderId(senderId)
                .body(request.body())
                .attachmentS3Key(request.attachmentS3Key())
                .attachmentName(request.attachmentName())
                .isSystemMessage(false)
                .build();

        message = messageRepository.save(message);

        thread.setLastMessageAt(Instant.now());
        if (request.senderType() == SenderType.STUDENT) {
            thread.setStaffUnreadCount(thread.getStaffUnreadCount() + 1);
        } else {
            thread.setStudentUnreadCount(thread.getStudentUnreadCount() + 1);
        }

        return SupportMessageResponse.from(message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportMessageResponse> findAllByThread(UUID threadUuid) {
        var thread = threadService.findThreadEntityByUuid(threadUuid);
        return messageRepository.findByThreadIdOrderByCreatedAtAsc(thread.getId())
                .stream().map(SupportMessageResponse::from).toList();
    }

    @Override
    public void markAsRead(UUID threadUuid, SenderType readerType, UUID readerUuid) {
        var thread = threadService.findThreadEntityByUuid(threadUuid);
        var readerId = resolveSenderId(readerType, readerUuid);

        var receiptId = new SupportReadReceiptId(thread.getId(), readerType, readerId);
        var now = Instant.now();

        var receipt = readReceiptRepository.findById(receiptId)
                .orElse(SupportReadReceipt.builder()
                        .id(receiptId)
                        .build());

        receipt.setLastReadAt(now);
        readReceiptRepository.save(receipt);

        if (readerType == SenderType.STUDENT) {
            thread.setStudentUnreadCount(0);
        } else {
            thread.setStaffUnreadCount(0);
        }
    }

    private Long resolveSenderId(SenderType senderType, UUID senderUuid) {
        return switch (senderType) {
            case STUDENT -> {
                var student = studentRepository.findByUuid(senderUuid)
                        .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
                yield student.getId();
            }
            case LECTURER, ADMIN -> {
                var appUser = appUserRepository.findByUuid(senderUuid)
                        .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
                yield appUser.getId();
            }
        };
    }
}
