FROM openjdk:25-ea-25-jdk-slim
WORKDIR /app
COPY target/module5-kafka-0.0.1-SNAPSHOT.jar notification-service.jar
ENTRYPOINT ["java", "-jar", "notification-service.jar"]
EXPOSE 8000