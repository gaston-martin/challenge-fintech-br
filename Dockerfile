#
# This is a multi-stage docker build.
# Gradle image for the build stage.
#
FROM gradle:8-jdk21-corretto AS build-image

#
# Set the working directory.
#
WORKDIR /app
#
# Build the application.
#
COPY . .
RUN ./gradlew clean build
#
# Java image for the application to run in.
#
FROM amazoncorretto:21-alpine-jdk
#
# Copy the jar file in and name it app.jar.
#
COPY --from=build-image /app/build/libs/challenge-*SNAPSHOT.jar app.jar
#
# This syntax forks the process and allows propagation of OS signals such as SIGKILL and SIGTERM
#
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]