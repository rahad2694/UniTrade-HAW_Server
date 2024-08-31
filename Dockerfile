FROM openjdk:21-jdk-slim
WORKDIR /app
COPY . /app
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .
COPY src /app/src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Copy the built JAR file to the final image
COPY target/uniTrade-0.0.1-SNAPSHOT.jar /app/uniTrade-0.0.1-SNAPSHOT.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/uniTrade-0.0.1-SNAPSHOT.jar"]
