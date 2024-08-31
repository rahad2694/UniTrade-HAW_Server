FROM openjdk:21-jdk-slim
WORKDIR /app
COPY . /app
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests
COPY --from=build /app/target/uniTrade-0.0.1-SNAPSHOT.jar /app/uniTrade.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
#CMD ["java", "-jar", "/app/uniTrade.jar"]

ENTRYPOINT ["java", "-jar", "/app/target/your-app.jar"]

# Copy the jar file from the build stage
