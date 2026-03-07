package com.edusync.api.announcement.controller;

import com.edusync.api.announcement.controller.api.AnnouncementApi;
import com.edusync.api.announcement.dto.AnnouncementRequest;
import com.edusync.api.announcement.dto.AnnouncementResponse;
import com.edusync.api.announcement.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AnnouncementController implements AnnouncementApi {

    private final AnnouncementService service;

    @Override
    public AnnouncementResponse create(AnnouncementRequest.Create request) {
        return service.create(request);
    }

    @Override
    public AnnouncementResponse update(UUID announcementUuid, AnnouncementRequest.Update request) {
        return service.update(announcementUuid, request);
    }

    @Override
    public AnnouncementResponse publish(UUID announcementUuid) {
        return service.publish(announcementUuid);
    }

    @Override
    public AnnouncementResponse archive(UUID announcementUuid) {
        return service.archive(announcementUuid);
    }

    @Override
    public AnnouncementResponse findByUuid(UUID announcementUuid) {
        return service.findByUuid(announcementUuid);
    }

    @Override
    public List<AnnouncementResponse> findAll(AnnouncementRequest.Filter filter) {
        return service.findAll(filter);
    }

    @Override
    public List<AnnouncementResponse> feed() {
        return service.findPublishedFeed();
    }

    @Override
    public void delete(UUID announcementUuid) {
        service.delete(announcementUuid);
    }
}
