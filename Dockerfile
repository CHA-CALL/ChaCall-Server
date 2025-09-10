FROM openjdk:17
EXPOSE 8000
COPY ./build/libs/*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]