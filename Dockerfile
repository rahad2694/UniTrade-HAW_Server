# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml into the container
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .

# Copy the source code into the container
COPY src /app/src

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Copy the built JAR file to the final image
COPY target/your-app.jar /app/your-app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/your-app.jar"]
