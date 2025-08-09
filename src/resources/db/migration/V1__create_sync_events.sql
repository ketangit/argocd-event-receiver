-- argocd-events-receiver/src/main/resources/db/migration/V1__create_sync_events.sql
CREATE TABLE IF NOT EXISTS sync_events (
  id BIGSERIAL PRIMARY KEY,
  fingerprint VARCHAR(255) NOT NULL UNIQUE,
  event VARCHAR(64) NOT NULL,
  app VARCHAR(255) NOT NULL,
  project VARCHAR(255),
  phase VARCHAR(64),
  sync_status VARCHAR(64),
  health_status VARCHAR(64),
  repo_url TEXT,
  target_revision VARCHAR(255),
  dest_server TEXT,
  dest_namespace VARCHAR(255),
  finished_at TIMESTAMPTZ NOT NULL,
  received_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  images_added JSONB,
  images_updated JSONB,
  images_deleted JSONB
);