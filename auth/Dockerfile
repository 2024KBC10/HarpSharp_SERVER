FROM openjdk:17-jdk

COPY auth-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=prod", "-jar", "./board-0.0.1-SNAPSHOT.jar"]