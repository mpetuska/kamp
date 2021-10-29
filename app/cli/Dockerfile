FROM gcr.io/distroless/java:11

WORKDIR app
COPY build/libs/*.jar cli.jar

ENTRYPOINT ["java", "-jar", "cli.jar"]
