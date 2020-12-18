FROM registry.gitlab.com/martynas.petuska/kamp/graalvm-ce-musl:20.3.0-java11 AS build
WORKDIR /build
COPY build/libs/app-*.jar app.jar
COPY build/libs/WEB-INF WEB-INF
RUN pwd && ls
RUN native-image --no-fallback --verbose \
    --report-unsupported-elements-at-runtime \
    --static \
    --libc=musl \
    -H:Log=registerResource \
    -H:-UseServiceLoaderFeature \
    -jar \
    app.jar \
    app-native

# ========================= UPX   =========================
FROM gruebel/upx as upx
WORKDIR /build
COPY --from=build /build/app-native run.fat
RUN upx --best --lzma -o /build/run /build/run.fat

# ========================= FINAL =========================
FROM alpine
WORKDIR /app
COPY --from=upx /build/run run
COPY --from=build /build/WEB-INF WEB-INF
ENV PORT=8000
EXPOSE $PORT
ENTRYPOINT ./run
