FROM openjdk:17

EXPOSE 8080

# The application's jar file
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
COPY ${JAR_FILE} FINDY_BE.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "FINDY_BE.jar"]