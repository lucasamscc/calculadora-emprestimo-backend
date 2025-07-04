FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw && ./mvnw package
ENTRYPOINT ["java", "-jar", "target/calculadora-0.0.1-SNAPSHOT.jar"]