FROM maven:3.9.9-amazoncorretto-21 AS builder

WORKDIR /app

COPY pom.xml pom.xml
COPY bootstrap/pom.xml bootstrap/pom.xml
COPY model/pom.xml model/pom.xml
COPY adapter/pom.xml adapter/pom.xml
COPY application/pom.xml application/pom.xml

RUN mvn dependency:go-offline

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/bootstrap/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
