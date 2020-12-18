# registry.gitlab.com/martynas.petuska/kamp/graalvm-ce-musl:20.3.0-java11
ARG graalvmVersion=20.3.0
ARG muslVersion=1.2.1
ARG zlibVersion=1.2.11
FROM oracle/graalvm-ce:${graalvmVersion}-java11 AS build
RUN yum install wget make gcc configure -y -v
ARG graalvmVersion
ARG muslVersion
ARG zlibVersion

WORKDIR /build
RUN wget https://musl.libc.org/releases/musl-${muslVersion}.tar.gz
RUN tar -xf musl-${muslVersion}.tar.gz
WORKDIR /build/musl-${muslVersion}
RUN ./configure --disable-shared --prefix=/opt/musl
RUN make
RUN make install

WORKDIR /build
RUN wget https://zlib.net/zlib-${zlibVersion}.tar.gz
RUN tar -xf zlib-${zlibVersion}.tar.gz
WORKDIR /build/zlib-${zlibVersion}
RUN ./configure --static --prefix=/opt/musl
RUN make

FROM oracle/graalvm-ce:${graalvmVersion}-java11
ARG graalvmVersion
ARG muslVersion
ARG zlibVersion
WORKDIR /build
COPY --from=build /build/musl-${muslVersion} musl
WORKDIR /build/musl
RUN make install
ENV PATH=/opt/musl/bin:$PATH
WORKDIR /build
COPY --from=build /build/zlib-${zlibVersion} zlib
WORKDIR /build/zlib
RUN make install
RUN gu install native-image
WORKDIR /
RUN rm -rf /build

