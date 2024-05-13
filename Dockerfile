# Use a imagem oficial do OpenJDK para executar a aplicação
FROM openjdk:17-jdk-alpine
# Variável para armazenar o diretório da aplicação dentro do container
ARG APP_DIR=/usr/app/
# Cria o diretório da aplicação
RUN mkdir -p $APP_DIR
# Define o diretório da aplicação como o diretório de trabalho
WORKDIR $APP_DIR
# Copia o arquivo .jar para o diretório da aplicação
COPY ./target/*.jar $APP_DIR
# Instala o netcat, que é necessário para o script de espera
RUN apk add --no-cache netcat-openbsd
# Copia o script de espera para o diretório da aplicação
COPY wait-for-db.sh $APP_DIR
# Torna o script de espera executável
RUN chmod +x wait-for-db.sh
# Expõe a porta 8080 para acesso à aplicação
EXPOSE 8080
# Comando para iniciar a aplicação
CMD ["./wait-for-db.sh", "db:5432", "--", "java", "-jar", "security-0.0.1-SNAPSHOT.jar"]