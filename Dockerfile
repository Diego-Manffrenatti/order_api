FROM ubuntu:latest
LABEL authors="diego"

# Usa a imagem do OpenJDK 17
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR da aplicação para o container
COPY target/order-api-0.0.1-SNAPSHOT.jar order-api.jar

# Expondo a porta da aplicação
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "order-api.jar"]
