package com.edusync.api.broadcast.service;

import com.edusync.api.broadcast.dto.BroadcastRecipientResponse;
import com.edusync.api.broadcast.dto.BroadcastRequest;
import com.edusync.api.broadcast.dto.BroadcastResponse;

import java.util.List;
import java.util.UUID;

public interface BroadcastService {

    BroadcastResponse createBroadcast(BroadcastRequest.Create request);

    List<BroadcastResponse> findAllBroadcasts(BroadcastRequest.Filter filter);

    BroadcastResponse findBroadcastByUuid(UUID uuid);

    List<BroadcastRecipientResponse> findBroadcastRecipients(UUID broadcastUuid);

    BroadcastRecipientResponse markBroadcastAsRead(UUID broadcastUuid, UUID studentUuid);
}
