FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

ENTRYPOINT ["java", "-jar", "etflux-1.0-SNAPSHOT.jar"]
