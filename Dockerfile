# Etape 1 : Build avec Maven (Utilisation de Java 17 pour correspondre a ton projet)
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

COPY . .

RUN mvn clean package -DskipTests -Dmaven.resources.skip=false

# Etape 2 : Execution avec JRE leger
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN mkdir -p /app/uploads/produits

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]