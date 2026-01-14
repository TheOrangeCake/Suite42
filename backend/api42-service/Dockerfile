# Build stage -> copy pom -> download dependency -> copy files -> build *.jar
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# Run stage -> set up user -> copy *.jar -> run *.jar
FROM eclipse-temurin:17-jre
RUN mkdir /opt/app
RUN groupadd spring && useradd -r -g spring spring
USER spring:spring
COPY --from=build /app/target/*.jar /opt/app/app.jar
EXPOSE 4444
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]