# --------------------------------------------
# 1) BUILD STAGE (compile + run tests)
# --------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21 as builder
WORKDIR /app

# Copy the pom.xml first (for dependency caching)
COPY pom.xml .

# Download dependencies (this helps cache them, so the build is faster next time)
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Run tests + package the JAR
RUN mvn clean package

# --------------------------------------------
# 2) RUNTIME STAGE (small JDK image)
# --------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21
WORKDIR /app

# Copy JAR from the build stage
COPY --from=builder /app/target/LibraryManagement-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your service runs on
EXPOSE 8080

# Final command to run your microservice
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

