# Use an official Maven image for the build environment
FROM maven:3.8.5-eclipse-temurin-17 AS build

# Define a build argument for service name (default to movie-service)
ARG SERVICE_NAME=movie-service

# Set the working directory inside the container
WORKDIR /app

# Copy the parent pom.xml (if needed)
COPY pom.xml .

# Copy all module directories and their pom.xml files
COPY api-gateway/pom.xml ./api-gateway/
COPY movie-starter-lib/pom.xml ./movie-starter-lib/
COPY auth-service/pom.xml ./auth-service/
COPY discovery-server/pom.xml ./discovery-server/
COPY event-backbone/pom.xml ./event-backbone/
COPY movie-service/pom.xml ./movie-service/
COPY payment-service/pom.xml ./payment-service/
COPY messaging-service/pom.xml ./messaging-service/
# Add more COPY commands for additional modules as needed

# Copy source directories for all modules
COPY api-gateway/src ./api-gateway/src
COPY movie-starter-lib/src ./movie-starter-lib/src
COPY auth-service/src ./auth-service/src
COPY discovery-server/src ./discovery-server/src
COPY event-backbone/src ./event-backbone/src
COPY movie-service/src ./movie-service/src
COPY payment-service/src ./payment-service/src
COPY messaging-service/src ./messaging-service/src
# Add more COPY commands for additional modules as needed

# Build the selected service module
RUN mvn clean package -DskipTests -pl ${SERVICE_NAME} -am

# Use an official OpenJDK runtime as the base image
FROM eclipse-temurin:17
ARG SERVICE_NAME=movie-service

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage (assuming it's built under /app/${SERVICE_NAME}/target/)
COPY --from=build /app/${SERVICE_NAME}/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
