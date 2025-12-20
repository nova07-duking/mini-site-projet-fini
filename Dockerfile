# Étape 1 : Build avec Maven (Utilisation de Java 17 pour correspondre à ton projet)
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Correction MalformedInputException : Force l'encodage UTF-8 au niveau de l'OS du conteneur
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# Copie du code source
COPY . .

# Build du JAR en forçant l'encodage UTF-8 pour Maven
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8

# Étape 2 : Exécution avec JRE léger
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copie du JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Création du dossier pour les images (très important pour ton ServiceProduit)
RUN mkdir -p /app/uploads/produits

# Exposition du port (Ton app semble être sur 8081 d'après tes logs précédents)
EXPOSE 8081

# Lancement avec le profil 'docker' pour utiliser application-docker.properties
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]