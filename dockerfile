FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY . .
RUN ./mvnw package
ENTRYPOINT ["java", "-jar", "target/calculadora-0.0.1-SNAPSHOT.jar"]