# Build stage
FROM maven:3.9.3-eclipse-temurin-21 AS build

WORKDIR /app

# Copy root POM and module POMs
COPY pom.xml .
COPY apigateway/pom.xml ./apigateway/pom.xml
COPY user-service/pom.xml ./user-service/pom.xml
COPY common-lib/pom.xml ./common-lib/pom.xml

# Copy source code for required modules
COPY apigateway ./apigateway
COPY user-service ./user-service
COPY common-lib ./common-lib

# Build only the api-gateway module
RUN mvn clean package -pl apigateway -am -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the JAR built in the previous stage
COPY --from=build /app/apigateway/target/*.jar ./app.jar

# Expose default port
EXPOSE 8080

# Pass environment variables from Railway
ENV PORT=8080
ENV AUTH_TOKEN_SECRET=${AUTH_TOKEN_SECRET}
ENV AUTH_TOKEN_EXPIRATION=${AUTH_TOKEN_EXPIRATION}

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
