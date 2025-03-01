FROM openjdk:21-jdk

WORKDIR /app

COPY target/SzyncPound-0.0.1-SNAPSHOT.jar app.jar

EXPOSE $PORT

ENTRYPOINT ["java", "-jar", "app.jar"]