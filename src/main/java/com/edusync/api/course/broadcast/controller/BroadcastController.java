package com.edusync.api.course.broadcast.controller;

import com.edusync.api.course.broadcast.controller.api.BroadcastApi;
import com.edusync.api.course.broadcast.dto.BroadcastRecipientResponse;
import com.edusync.api.course.broadcast.dto.BroadcastRequest;
import com.edusync.api.course.broadcast.dto.BroadcastResponse;
import com.edusync.api.course.broadcast.enums.BroadcastPriority;
import com.edusync.api.course.broadcast.enums.BroadcastTarget;
import com.edusync.api.course.broadcast.service.BroadcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class BroadcastController implements BroadcastApi {

    private final BroadcastService service;

    @Override
    public BroadcastResponse create(BroadcastRequest.Create request) {
        return service.createBroadcast(request);
    }

    @Override
    public List<BroadcastResponse> findAll(UUID courseUuid, BroadcastTarget targetType, BroadcastPriority priority, String search) {
        return service.findAllBroadcasts(courseUuid, targetType, priority, search);
    }

    @Override
    public BroadcastResponse findByUuid(UUID broadcastUuid) {
        return service.findBroadcastByUuid(broadcastUuid);
    }

    @Override
    public List<BroadcastRecipientResponse> findRecipients(UUID broadcastUuid) {
        return service.findBroadcastRecipients(broadcastUuid);
    }

    @Override
    public BroadcastRecipientResponse markAsRead(UUID broadcastUuid, UUID studentUuid) {
        return service.markBroadcastAsRead(broadcastUuid, studentUuid);
    }
}
