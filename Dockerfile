# argocd-events-receiver/Dockerfile
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/argocd-events-receiver-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

ENTRYPOINT ["java","-jar","/app/app.jar"]
