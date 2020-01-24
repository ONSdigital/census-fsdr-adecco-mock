FROM openjdk:11-jdk-slim
ARG jar
COPY $jar /opt/mock.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "java", "-jar", "/opt/mock.jar" ]
