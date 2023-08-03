FROM gradle:6.9.4-jdk8 AS build-backend
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM amazoncorretto:8-alpine-jre
WORKDIR /backend
COPY --from=build-backend /home/gradle/src/build/libs/realtime-publisher-java.jar ./realtime-publisher-java.jar

ENV MAIN com.real.time.publisher.amsterdam.parking.App

ENTRYPOINT exec java -cp realtime-publisher-java.jar $MAIN