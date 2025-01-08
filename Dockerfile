FROM maven:3.9.6-eclipse-temurin-21 as builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests
FROM maven:3.9.6-eclipse-temurin-21
WORKDIR /app
COPY --from=builder /app/target/LibraryManagement-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
