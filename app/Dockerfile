FROM adoptopenjdk:15-jre-hotspot
WORKDIR /app
COPY build/libs/app-jvm*.jar app.jar
ENV PORT=8000
EXPOSE $PORT
ENTRYPOINT java -jar app.jar
