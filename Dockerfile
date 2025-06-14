FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean install spring-boot:repackage

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/university-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
