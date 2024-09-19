FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /app

COPY gradlew .
COPY gradle gradle

COPY build.gradle settings.gradle ./

COPY src src

RUN chmod +x gradlew

RUN ./gradlew build --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/yalli-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
