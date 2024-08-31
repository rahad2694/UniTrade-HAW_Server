# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim AS build

# Set the working directory in the container for the build
WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml .
COPY src ./src

# Build the project to create the jar file
RUN ./mvnw clean package -DskipTests

# Use a minimal base image for running the application
FROM openjdk:21-jdk-slim

# Set the working directory in the container for the application
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]
