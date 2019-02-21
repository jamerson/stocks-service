FROM openjdk:8-jdk-alpine

ARG jarFile
ENV IN_MEM_PERSIST redis
ADD ./target/${jarFile} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
