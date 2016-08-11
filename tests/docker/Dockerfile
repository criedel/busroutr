from ubuntu:16.04

RUN apt-get update && apt-get install -y \
    openjdk-8-jdk \
    git \
    maven \
    gradle \
 && rm -rf /var/lib/apt/lists/*

COPY example /data/example
ARG repo
WORKDIR /data
RUN git clone $repo dev-test
WORKDIR dev-test
RUN bash build.sh
