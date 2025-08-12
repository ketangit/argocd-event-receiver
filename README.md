# Argo CD Events Receiver

This service handles ArgoCD post-sync hooks to persist success and failure sync events to PostgreSQL. It enriches sync success webhooks with Docker image details.

## Features

- Receives sync success/failure events from Argo CD Notifications
- Stores normalized event data in PostgreSQL
- Supports JSONB storage of added/updated/deleted container images
- Secured via shared token header
- Flyway-managed schema
- Kubernetes-ready deployment


### Build the Application
```bash
make build
```

### Run the Application
```bash
make run
```


### Test the Application
```bash
make test
```


## Configure ArgoCD
To configure ArgoCD to use post-sync hooks, add the following to your ArgoCD application manifest:
```yaml
hooks:
  - type: PostSync
    command: ["curl"]
    args: ["-X", "POST", "http://<service-url>/webhook", "-d", "@sync-event.json"]
```


### Push and deploy to Kubernetes
```bash
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f argocd-config/argocd-notifications-secret.yaml
kubectl apply -f argocd-config/argocd-notifications-cm.yaml
```


### Annotate Argo CD Applications
Add these annotations to each Application you want to monitor:
```yaml
metadata:
  annotations:
    notifications.argoproj.io/subscribe.on-sync-succeeded.webhook.spring-receiver: ""
    notifications.argoproj.io/subscribe.on-sync-failed.webhook.spring-receiver: ""
```


### Optional: PostSync Hook to send image metadata
You can define a Job like this in your app repo
```yaml
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
```
