FROM openjdk:15-jre-alpine
WORKDIR /app
COPY build/libs/app-*.jar app.jar
ENV PORT=8000
EXPOSE $PORT
ENTRYPOINT java -jar app.jar
