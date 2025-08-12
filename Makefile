APP_NAME=argocd-events-receiver
VERSION=0.0.1-SNAPSHOT
IMAGE=your-registry/$(APP_NAME):$(VERSION)

build:
	mvn clean package -DskipTests

run:
	SPRING_PROFILES_ACTIVE=local java -jar target/$(APP_NAME)-$(VERSION).jar

test:
	mvn test

docker-build:
	docker build -t $(IMAGE) .

docker-run:
	docker run -p 8080:8080 $(IMAGE)