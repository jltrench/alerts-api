FROM maven:3.8.1-openjdk-17-slim AS build

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

ARG APP_DIR=/usr/app/

RUN mkdir -p $APP_DIR

WORKDIR $APP_DIR

COPY --from=build /workspace/target/*.jar $APP_DIR

RUN apk add --no-cache netcat-openbsd

COPY wait-for-db.sh $APP_DIR

RUN chmod +x wait-for-db.sh

EXPOSE 8080

CMD ["java", "-jar", "security-0.0.1-SNAPSHOT.jar"]