package io.ketan.events.repository;

import io.ketan.events.model.SyncEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SyncEventRepository extends JpaRepository<SyncEvent, Long> {
  Optional<SyncEvent> findByFingerprint(String fingerprint);
}