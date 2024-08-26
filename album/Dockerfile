FROM openjdk:17-jdk

COPY todo-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "./album-0.0.1-SNAPSHOT.jar"]