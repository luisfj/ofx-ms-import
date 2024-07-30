FROM openjdk:21-jdk-slim
EXPOSE 8040
ADD target/ofx-ms-import.jar ofx-ms-import.jar
ENTRYPOINT [ "java", "-jar", "/ofx-ms-import.jar" ]