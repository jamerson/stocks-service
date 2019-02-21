# Stocks Service

Stocks Management service using Spring Boot, Redis, Docker and Docker Compose.

## Prerequisites

- [Docker](https://www.docker.com/)
- [Docker Compose](https://github.com/docker/compose)

__Note:__ To re-build the code, run the test suite or re-package the solution, [Java 8](https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html) is needed. The service uses [Maven](https://maven.apache.org/) to build, run unit tests and create the target JAR file, but it is not necessary to have it installed, use the [Maven Wrapper](https://github.com/takari/maven-wrapper) binary present in project folder.

## Getting started

1. Clone this repository

2. Run the following commands on a terminal:

```sh
docker-compose up
```

### Usage

### Unit tests

### Integration tests

## Building

Run the following command to re-build and generate a new JAR file:
```sh
./mvnw clean package
```

A new `./target/stocks-service-service-1.0-SNAPSHOT.jar` will be created. After that the `springboot` image needs to be recreated.

1. Stop and destroy the current containers, first:

```sh
docker-compose down
```

2. Rebuild the images:

```sh
docker-compose build --no-cache
```

3. Start the containers:

```sh
docker-compose up
```
