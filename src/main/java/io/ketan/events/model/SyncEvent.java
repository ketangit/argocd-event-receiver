package io.ketan.events.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "sync_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_name", nullable = false)
    private String appName;

    @Column(name = "project")
    private String project;

    @Column(name = "revision")
    private String revision;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SyncStatus status;

    @Column(name = "sync_started_at")
    private Instant syncStartedAt;

    @Column(name = "sync_completed_at")
    private Instant syncCompletedAt;

    @Column(name = "fingerprint", nullable = false, unique = true)
    private String fingerprint;

    @ElementCollection
    @CollectionTable(name = "sync_event_images", joinColumns = @JoinColumn(name = "sync_event_id"))
    private List<ContainerImage> images;

    public enum SyncStatus {
        SUCCESS,
        FAILURE,
        UNKNOWN
    }
}