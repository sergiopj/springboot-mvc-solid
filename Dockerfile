# Imagen base ligera con Java 21 JRE
FROM eclipse-temurin:21-jre-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR compilado por Maven
COPY target/*.jar app.jar

# Expone el puerto 8080 para la API
EXPOSE 8080

# Comando para iniciar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
