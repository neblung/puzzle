# Use the official gradle image to create a build artifact.
# https://hub.docker.com/_/gradle
FROM gradle:6.8 as builder

# Copy local code to the container image.
COPY build.gradle.kts .
COPY src ./src

# Build an executable jar file
RUN gradle uberJar --no-daemon

# Use the Official OpenJDK image for a lean production stage of our multi-stage build.
# https://hub.docker.com/_/openjdk
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM openjdk:8-jre-alpine

# Copy the jar to the production image from the builder stage.
COPY --from=builder /home/gradle/build/libs/uber.jar /puzzle.jar

# Run program
CMD [ "java", "-jar", "/puzzle.jar" ]