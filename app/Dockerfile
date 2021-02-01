FROM openjdk:15-alpine
WORKDIR /app
COPY build/libs/app-jvm*.jar app.jar
ENV PORT=8000
EXPOSE $PORT
ENTRYPOINT java -jar app.jar
