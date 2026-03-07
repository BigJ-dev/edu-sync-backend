package com.edusync.api.announcement.service;

import com.edusync.api.announcement.dto.AnnouncementRequest;
import com.edusync.api.announcement.dto.AnnouncementResponse;

import java.util.List;
import java.util.UUID;

public interface AnnouncementService {

    AnnouncementResponse create(AnnouncementRequest.Create request);

    AnnouncementResponse update(UUID announcementUuid, AnnouncementRequest.Update request);

    AnnouncementResponse publish(UUID announcementUuid);

    AnnouncementResponse archive(UUID announcementUuid);

    AnnouncementResponse findByUuid(UUID announcementUuid);

    List<AnnouncementResponse> findAll(AnnouncementRequest.Filter filter);

    List<AnnouncementResponse> findPublishedFeed();

    void delete(UUID announcementUuid);
}
