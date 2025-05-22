FROM amazoncorretto:17-alpine-jdk

WORKDIR /app
COPY build/app.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
