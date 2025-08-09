package io.ketan.events.service;

import io.ketan.events.model.SyncEvent;
import io.ketan.events.model.SyncEvent.SyncStatus;
import io.ketan.events.model.ContainerImage;
import io.ketan.events.repository.SyncEventRepository;
import io.ketan.events.webhook.ArgoCdEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SyncEventService {

    private final SyncEventRepository repository;

    public SyncEvent saveFromPayload(ArgoCdEventPayload payload) {
        SyncEvent event = mapPayloadToEntity(payload);
        return repository.save(event);
    }

    private SyncEvent mapPayloadToEntity(ArgoCdEventPayload payload) {
        return SyncEvent.builder()
                .appName(payload.getAppName())
                .project(payload.getProject())
                .revision(payload.getRevision())
                .status(parseStatus(payload.getStatus()))
                .syncStartedAt(parseInstant(payload.getSyncStartedAt()))
                .syncCompletedAt(parseInstant(payload.getSyncCompletedAt()))
                .fingerprint(payload.getFingerprint())
                .images(mapImages(payload.getImages()))
                .build();
    }

    private SyncStatus parseStatus(String status) {
        if (status == null) return SyncStatus.UNKNOWN;
        return switch (status.toLowerCase()) {
            case "success" -> SyncStatus.SUCCESS;
            case "failure" -> SyncStatus.FAILURE;
            default -> SyncStatus.UNKNOWN;
        };
    }

    private Instant parseInstant(String timestamp) {
        try {
            return timestamp != null ? Instant.parse(timestamp) : null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private List<ContainerImage> mapImages(List<ContainerImage> images) {
        return images != null ? images : List.of();
    }
}