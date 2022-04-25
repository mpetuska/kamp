FROM gcr.io/distroless/java:11

WORKDIR app
COPY build/libs/*-jvm-*.jar scanner.jar

ENTRYPOINT ["java", "-jar", "scanner.jar"]
