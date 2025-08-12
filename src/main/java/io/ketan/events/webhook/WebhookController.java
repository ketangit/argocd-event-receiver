package io.ketan.events.webhook;

import io.ketan.events.model.SyncEvent;
import io.ketan.events.model.ContainerImage;
import io.ketan.events.service.SyncEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);
    private final SyncEventService eventService;

    public WebhookController(SyncEventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/argocd")
    public ResponseEntity<?> receiveSyncEvent(
            @RequestHeader(value = "X-Token", required = false) String token,
            @RequestBody ArgoCdEventPayload payload
    ) {
        if (!eventService.isTokenValid(token)) {
            log.warn("Unauthorized webhook attempt for app: {}", payload.getAppName());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        List<ContainerImage> images = payload.getImages() != null ? payload.getImages() : List.of();
        SyncEvent saved = eventService.persistEvent(payload, images);

        log.info("Stored sync event for app: {}, revision: {}", saved.getAppName(), saved.getRevision());
        return ResponseEntity.ok("Event stored");
    }
}