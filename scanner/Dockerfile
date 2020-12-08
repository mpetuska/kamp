FROM openjdk:15-alpine

WORKDIR app
COPY build/libs/*-all.jar app.jar

ENTRYPOINT java -jar app.jar
