FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY target/etflux-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
