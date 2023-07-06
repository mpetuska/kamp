FROM gcr.io/distroless/java:11

WORKDIR app
COPY build/libs/cli-jvm-*-fat.jar cli.jar

ENTRYPOINT ["java", "-jar", "cli.jar"]
