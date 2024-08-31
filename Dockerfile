# Use an official OpenJDK runtime as a parent image
#FROM openjdk:21-jdk-slim
FROM openjdk:21-jdk

# First stage: build the application
#FROM maven:3.8.6-openjdk-21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy Maven wrapper and other necessary files
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .

# Copy the source code and build it
COPY src /app/src

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# List files in target directory for debugging
RUN ls -al /app/target

# Copy the built JAR file to the final image
COPY --from=build /app/target/uniTrade-0.0.1-SNAPSHOT.jar /app/uniTrade-0.0.1-SNAPSHOT.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/uniTrade-0.0.1-SNAPSHOT.jar"]
