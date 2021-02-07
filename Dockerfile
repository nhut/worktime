#--BUILD--
FROM maven:3.6.1-jdk-8-alpine as MAVEN_BUILD
MAINTAINER mr.nhut@gmail.com

WORKDIR /build
COPY pom.xml /build/
COPY client-phz /build/client-phz
COPY web /build/web

RUN mvn package

#--RELEASE--
FROM openjdk:8-jre-alpine

WORKDIR /app
COPY --from=MAVEN_BUILD /build/web/target/web-1.0.0.jar /app/

ENV spring.profiles.active=prod
EXPOSE 443
ENTRYPOINT ["java","-jar","web-1.0.0.jar"]
