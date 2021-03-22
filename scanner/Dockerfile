FROM gcr.io/distroless/java:11

WORKDIR app
COPY build/libs/*.jar scanner.jar

ENTRYPOINT ["java", "-jar", "scanner.jar"]
