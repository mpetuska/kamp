FROM gcr.io/distroless/java:11

WORKDIR /app
COPY build/libs/server-jvm*-fat.jar app.jar
ENV PORT=8080
EXPOSE $PORT
ENTRYPOINT ["java", "-jar", "app.jar"]
