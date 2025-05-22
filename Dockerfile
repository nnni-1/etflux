FROM amazoncorretto:17-alpine-jdk

WORKDIR /app
COPY build/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
