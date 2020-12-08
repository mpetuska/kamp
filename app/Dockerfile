FROM openjdk:15-alpine

WORKDIR app
COPY build/libs/*-all.jar app.jar

EXPOSE 8080
ENTRYPOINT java -jar app.jar
