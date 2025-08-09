APP_NAME=argocd-events-receiver
VERSION=1.0.0
IMAGE=your-registry/$(APP_NAME):$(VERSION)

build:
	mvn clean package -DskipTests

run:
	java -jar target/$(APP_NAME)-$(VERSION).jar

docker-build:
	docker build -t $(IMAGE) .

docker-run:
	docker run -p 8080:8080 $(IMAGE)