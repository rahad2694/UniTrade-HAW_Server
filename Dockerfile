# First stage: build the application
FROM maven:3.8.6-openjdk-21 AS build

WORKDIR /app

# Copy Maven wrapper and configuration files
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .

# Copy the source code and build the JAR file
COPY src /app/src
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Second stage: create the final image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/uniTrade-0.0.1-SNAPSHOT.jar /app/uniTrade-0.0.1-SNAPSHOT.jar

# Command to run the application
CMD ["java", "-jar", "/app/uniTrade-0.0.1-SNAPSHOT.jar"]
