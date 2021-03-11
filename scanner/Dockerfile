FROM openjdk:15-alpine

WORKDIR app
COPY build/libs/*.jar scanner.jar

ENTRYPOINT java -jar scanner.jar
