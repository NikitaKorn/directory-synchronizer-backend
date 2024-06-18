FROM cr.yandex/mirror/alpine:latest

RUN apk add openjdk17
COPY target/directory-synchronizer-backend-1.0.0.jar app.jar

VOLUME /repository/files

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080