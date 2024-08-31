# Stage 1: Build the application
FROM maven:3.8.1-openjdk-21 AS build

# Set the working directory
WORKDIR /app

# Copy the project files
COPY pom.xml .

# Build the project
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/uniTrade-0.0.1-SNAPSHOT.jar /app/uniTrade.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/uniTrade.jar"]