# Argo CD Events Receiver

A Spring Boot 3.x application that receives Argo CD sync notifications via webhook and stores metadata—including Docker image changes—in PostgreSQL.

## Features

- Receives sync success/failure events from Argo CD Notifications
- Stores normalized event data in PostgreSQL
- Supports JSONB storage of added/updated/deleted container images
- Secured via shared token header
- Flyway-managed schema
- Kubernetes-ready deployment

## Setup

### 1. Build & Run Locally

```bash
make build
make run
```
# argocd-event-receiver
Argo CD sync notifications


1. Build the JAR
cd argocd-events-receiver
mvn clean package

2. Build Docker image
make docker-build
make docker-run

3. Push and deploy to Kubernetes
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f argocd-config/argocd-notifications-secret.yaml
kubectl apply -f argocd-config/argocd-notifications-cm.yaml



3. Annotate Argo CD Applications
Add these annotations to each Application you want to monitor:

```yaml
metadata:
  annotations:
    notifications.argoproj.io/subscribe.on-sync-succeeded.webhook.spring-receiver: ""
    notifications.argoproj.io/subscribe.on-sync-failed.webhook.spring-receiver: ""
```

4. Optional: PostSync Hook to send image metadata
You can define a Job like this in your app repo

apiVersion: batch/v1
kind: Job
metadata:
  name: post-sync-image-collector
  annotations:
    argocd.argoproj.io/hook: PostSync
    argocd.argoproj.io/hook-delete-policy: HookSucceeded
spec:
  template:
    spec:
      containers:
      - name: collector
        image: ghcr.io/your-org/image-collector:latest
        env:
        - name: RECEIVER_URL
          value: http://argocd-events-receiver.default.svc:8080/webhook/images
        command: ["node", "collect-and-send.js"]
      restartPolicy: Never
