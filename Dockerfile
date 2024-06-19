# FROM cr.yandex/mirror/alpine:3.20
FROM alpine:3.20

RUN apk add openjdk17
COPY target/directory-synchronizer-backend-1.0.1.jar app.jar

VOLUME /repository/files

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080