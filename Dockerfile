FROM openjdk:21-jdk-slim
WORKDIR /app
COPY . /app
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
ENTRYPOINT ["java", "-jar", "/app/target/uniTrade-0.0.1-SNAPSHOT.jar"]
