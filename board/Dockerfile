FROM openjdk:17-jdk

COPY board-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "./board-0.0.1-SNAPSHOT.jar"]