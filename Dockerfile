#
#FROM amazoncorretto:17-alpine-jdk AS builder
#
#
#WORKDIR /app
#
#COPY . .
#
#RUN ./gradlew clean build -x test
#
#
#
FROM amazoncorretto:17-alpine-jdk AS builder

CMD ["./gradlew", "clean", "build"]


VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar


COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]