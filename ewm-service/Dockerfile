FROM amazoncorretto:11.0.12-alpine-jdk
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]