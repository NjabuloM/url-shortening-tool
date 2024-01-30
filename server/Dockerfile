#Build phase
FROM maven:3.9-eclipse-temurin-17-alpine AS build
COPY . /app
WORKDIR /app
RUN mvn package

FROM openjdk:17-jdk-slim
COPY --from=build /app/target/url-shortening-tool-1.0.0.jar /url-shortening-tool.jar
ENTRYPOINT ["java", "-jar", "/url-shortening-tool.jar"]

#FROM openjdk:17-jdk-slim

#FROM registry.access.redhat.com/ubi8/openjdk-17:1.16

#COPY --chown=185 target/url-shortening-tool-1.0.0.jar /deployments/url-shortening-tool-1.0.0.jar
#ENTRYPOINT ["java","-jar","/url-shortening-tool-1.0.0.jar"]
#ENTRYPOINT ["java","-cp","app:app/lib/*","dev.njabulo.korturl.ShortURLApplication"]