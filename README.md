# Stocks Service

Stocks Management service using Spring Boot, Redis, Docker and Docker Compose.

The service should manage a list of stocks, it should be considered as a MVP, so simplicity and fast development are a must, but without forgeting that it should be read for production.

The stock market is fast paced and very volatile, so makes sense to think that the service will receive a lot of requests, that can be separated in third-party services sending requests to update stocks information and consumers retrieving stocks information. 

The consumers expect to get the latest information fast, so the new stocks and updated information of available stocks should be available to be retrieved as fast as possible, the service should handle a lot of concurrent access of third-party services updating their stocks information and consumers retrieving the latest information.

An REST API is provided to enable comunication with other services, the data is stored in memory for fast access.

A docker compose file is provided to start the the necessary services, the possible configurations are:

- __SERVICE_PORT__: The Stock service port
- __IN_MEM_PERSIST__: The persistence method used, it can be: `map`, were the stocks are stored in a `ConcurrentHashMap` data structure, and `redis`, were the stocks are stored in a Redis server configured with in-memory persistence only.

## Prerequisites

- [Docker](https://www.docker.com/)
- [Docker Compose](https://github.com/docker/compose)

__Note:__ To re-build the code, run the test suite or re-package the solution, [Java 8](https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html) is required. The service uses [Maven](https://maven.apache.org/) to build, run unit tests and create the target JAR file, but it is not necessary to have it installed, use the [Maven Wrapper](https://github.com/takari/maven-wrapper) binary present in project folder.

## Getting started

1. Clone this repository

2. Run the following commands on a terminal:

```sh
docker-compose up
```

Compose will start two containers (building the images if necessary), a Redis instance and a Spring Boot instance serving the content of this repository, provided as `./target/stocks-service-0.0.1-SNAPSHOT.jar` JAR file during the image building process.

The service will be up and running when printing a message similar to this:

```sh
2019-02-21 13:55:05.846  INFO 22321 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path '/api'
```

The service will be available in the port 8080. (this port can be changed in Docker compose file or setting the `SERVICE_PORT` environment variable).

To run the service using the Hash Map persistence method, it is necessary to set the `IN_MEM_PERSIST` environment variable:

```sh
IN_MEM_PERSIST=map docker-compose up springboot
```

Sample stock data is loaded during the service startup, this information can be customized in `stockservice/application.yml` file.

The service provides the following endpoints:

### Usage

- GET `/api/stocks`: Get a list of stocks
- GET `/api/stocks/{id}`: Get one stock from the list
- PUT `/api/stocks/{id}`: Update the price of a single stock using the given payload information
- POST `/api/stocks`: Create a stock using the given payload information

The API documentation is available at `/api/swagger-ui.html`.

### Unit tests

During the package creation, all unit test are executed. To re-executed them, go to `stockservice` folder and execute the following command:

```sh
./mvnw test
```

The test files are located under `stockservice/src/test` folder.

### Integration tests

1. The integration tests use the Redis service, run the following command to start the container:

```sh
docker-compose up redis
```

2. Got to `stocksservice` folder and run:

```sh
./mvnw integration-test
```

## Building

Got to `stocksservice` folder and run the following command to re-build and generate a new JAR file:

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
