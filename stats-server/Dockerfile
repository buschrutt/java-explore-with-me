FROM amazoncorretto:11.0.12-alpine-jdk
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9091
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
HEALTHCHECK CMD curl --fail http://localhost:9091/actuator/health || exit 1
