FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY . .

RUN ./mvnw clean package

#### 

FROM eclipse-temurin:21-jdk-alpine

COPY --from=build /app/target/library-0.0.1-SNAPSHOT.jar /app.jar

ENV SERVER_PORT=8080
ENV POSTGRES_URL=localhost
ENV POSTGRES_PORT=5432
ENV POSTGRES_DB=events
ENV POSTGRES_USERNAME=
ENV POSTGRES_PASSWORD=
ENV SHOW_SQL=false

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app.jar" ]

