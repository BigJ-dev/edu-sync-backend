package com.edusync.api.course.broadcast.service;

import com.edusync.api.course.broadcast.dto.BroadcastRecipientResponse;
import com.edusync.api.course.broadcast.dto.BroadcastRequest;
import com.edusync.api.course.broadcast.dto.BroadcastResponse;

import java.util.List;
import java.util.UUID;

public interface BroadcastService {

    BroadcastResponse createBroadcast(BroadcastRequest.Create request);

    List<BroadcastResponse> findAllBroadcasts(BroadcastRequest.Filter filter);

    BroadcastResponse findBroadcastByUuid(UUID uuid);

    List<BroadcastRecipientResponse> findBroadcastRecipients(UUID broadcastUuid);

    BroadcastRecipientResponse markBroadcastAsRead(UUID broadcastUuid, UUID studentUuid);
}
