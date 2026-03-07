package com.edusync.api.announcement.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.announcement.dto.AnnouncementRequest;
import com.edusync.api.announcement.dto.AnnouncementResponse;
import com.edusync.api.announcement.enums.AnnouncementStatus;
import com.edusync.api.announcement.model.Announcement;
import com.edusync.api.announcement.repo.AnnouncementRepository;
import com.edusync.api.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public AnnouncementResponse create(AnnouncementRequest.Create request) {
        var publisher = findAppUser(request.publishedByUuid());

        var announcement = Announcement.builder()
                .title(request.title())
                .body(request.body())
                .category(request.category())
                .status(AnnouncementStatus.DRAFT)
                .pinned(request.pinned())
                .publishedBy(publisher)
                .expiresAt(request.expiresAt())
                .attachmentS3Key(request.attachmentS3Key())
                .attachmentName(request.attachmentName())
                .build();

        return AnnouncementResponse.from(announcementRepository.save(announcement));
    }

    @Override
    public AnnouncementResponse update(UUID announcementUuid, AnnouncementRequest.Update request) {
        var announcement = findAnnouncement(announcementUuid);

        if (Objects.nonNull(request.title())) announcement.setTitle(request.title());
        if (Objects.nonNull(request.body())) announcement.setBody(request.body());
        if (Objects.nonNull(request.category())) announcement.setCategory(request.category());
        announcement.setPinned(request.pinned());
        announcement.setExpiresAt(request.expiresAt());
        announcement.setAttachmentS3Key(request.attachmentS3Key());
        announcement.setAttachmentName(request.attachmentName());

        return AnnouncementResponse.from(announcementRepository.save(announcement));
    }

    @Override
    public AnnouncementResponse publish(UUID announcementUuid) {
        var announcement = findAnnouncement(announcementUuid);

        if (announcement.getStatus() == AnnouncementStatus.PUBLISHED) {
            throw new ServiceException(HttpStatus.CONFLICT, "Announcement is already published");
        }

        announcement.setStatus(AnnouncementStatus.PUBLISHED);
        announcement.setPublishedAt(Instant.now());

        return AnnouncementResponse.from(announcementRepository.save(announcement));
    }

    @Override
    public AnnouncementResponse archive(UUID announcementUuid) {
        var announcement = findAnnouncement(announcementUuid);

        if (announcement.getStatus() == AnnouncementStatus.ARCHIVED) {
            throw new ServiceException(HttpStatus.CONFLICT, "Announcement is already archived");
        }

        announcement.setStatus(AnnouncementStatus.ARCHIVED);

        return AnnouncementResponse.from(announcementRepository.save(announcement));
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponse findByUuid(UUID announcementUuid) {
        return AnnouncementResponse.from(findAnnouncement(announcementUuid));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponse> findAll(AnnouncementRequest.Filter filter) {
        var spec = Stream.of(
                        AnnouncementSpec.hasCategory(filter.category()),
                        AnnouncementSpec.hasStatus(filter.status()),
                        AnnouncementSpec.isPinned(filter.pinned()),
                        AnnouncementSpec.searchByTitle(filter.search()))
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());

        return announcementRepository.findAll(spec, Sort.by(Sort.Order.desc("pinned"), Sort.Order.desc("publishedAt")))
                .stream().map(AnnouncementResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponse> findPublishedFeed() {
        var spec = AnnouncementSpec.hasStatus(AnnouncementStatus.PUBLISHED);

        return announcementRepository.findAll(spec, Sort.by(Sort.Order.desc("pinned"), Sort.Order.desc("publishedAt")))
                .stream()
                .filter(a -> Objects.isNull(a.getExpiresAt()) || a.getExpiresAt().isAfter(Instant.now()))
                .map(AnnouncementResponse::from)
                .toList();
    }

    @Override
    public void delete(UUID announcementUuid) {
        var announcement = findAnnouncement(announcementUuid);
        announcementRepository.delete(announcement);
    }

    private Announcement findAnnouncement(UUID uuid) {
        return announcementRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Announcement was not found"));
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }
}
