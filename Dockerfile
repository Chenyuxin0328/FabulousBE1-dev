FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/Fabulous-0.0.1-SNAPSHOT.jar .
EXPOSE 5083
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "Fabulous-0.0.1-SNAPSHOT.jar"]