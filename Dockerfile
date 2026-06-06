FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline

COPY src src
COPY sql sql
COPY README.md .

RUN mvn -B -DskipTests clean package

FROM tomcat:10.1.34-jre17-temurin

ENV PORT=8080

WORKDIR /usr/local/tomcat

RUN rm -rf webapps/*

COPY --from=build /app/target/ordena-space.war webapps/ROOT.war
COPY docker/entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]
