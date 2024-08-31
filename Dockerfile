# Use the official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper related files
COPY .mvn/ .mvn/
COPY mvnw .

# Set execute permissions on the Maven wrapper script
RUN chmod +x mvnw

# Copy the pom.xml file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the project to create the jar file
RUN ./mvnw clean package -DskipTests

# Use a minimal base image for running the application
# (You might add additional steps here for running the application)
