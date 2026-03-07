package com.edusync.api.course.messaging.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.messaging.dto.MessageRequest;
import com.edusync.api.course.messaging.dto.MessageResponse;
import com.edusync.api.course.messaging.enums.SenderType;
import com.edusync.api.course.messaging.model.Message;
import com.edusync.api.course.messaging.model.MessageReadReceipt;
import com.edusync.api.course.messaging.model.MessageReadReceiptId;
import com.edusync.api.course.messaging.model.MessageThread;
import com.edusync.api.course.messaging.repo.MessageReadReceiptRepository;
import com.edusync.api.course.messaging.repo.MessageRepository;
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
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageReadReceiptRepository readReceiptRepository;
    private final MessageThreadService threadService;
    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public MessageResponse addMessageToThread(UUID threadUuid, MessageRequest.Create request) {
        var thread = threadService.findMessageThreadEntityByUuid(threadUuid);
        var senderId = resolveSenderId(request.senderType(), request.senderUuid());

        var message = Message.builder()
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

        return MessageResponse.from(message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> findAllMessagesByThread(UUID threadUuid) {
        var thread = threadService.findMessageThreadEntityByUuid(threadUuid);
        return messageRepository.findByThreadIdOrderByCreatedAtAsc(thread.getId())
                .stream().map(MessageResponse::from).toList();
    }

    @Override
    public void markMessagesAsRead(UUID threadUuid, SenderType readerType, UUID readerUuid) {
        var thread = threadService.findMessageThreadEntityByUuid(threadUuid);
        var readerId = resolveSenderId(readerType, readerUuid);

        var receiptId = new MessageReadReceiptId(thread.getId(), readerType, readerId);
        var now = Instant.now();

        var receipt = readReceiptRepository.findById(receiptId)
                .orElse(MessageReadReceipt.builder()
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
