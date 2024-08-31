FROM openjdk:21-jdk-slim
WORKDIR /app
COPY . /app
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
ENTRYPOINT ["java", "-jar", "/app/target/your-app.jar"]
