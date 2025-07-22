# Usa una imagen de Java ligera
FROM openjdk:21-jdk-slim

# Crea un directorio para la app
WORKDIR /appFarmacos

# Copia el .jar al contenedor
COPY target/farmacos-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que usa Spring Boot
EXPOSE 8080

# Inicia la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
