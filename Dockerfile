FROM openjdk:17-jdk-alpine

ARG APP_DIR=/usr/app/

RUN mkdir -p $APP_DIR

WORKDIR $APP_DIR

COPY ./target/*.jar $APP_DIR

RUN apk add --no-cache netcat-openbsd

COPY wait-for-db.sh $APP_DIR

RUN chmod +x wait-for-db.sh

EXPOSE 8080

CMD ["./wait-for-db.sh", "db:5432", "--", "java", "-jar", "security-0.0.1-SNAPSHOT.jar"]