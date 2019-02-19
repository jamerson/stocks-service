FROM openjdk:8-jdk-alpine

ARG jarFile
ADD ./target/${jarFile} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=redis","-jar","/app.jar"]
