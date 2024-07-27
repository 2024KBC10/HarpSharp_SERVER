FROM openjdk:17-jdk

COPY auth-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "./auth-0.0.1-SNAPSHOT.jar"]