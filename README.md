# Stocks Service

Stocks Management service using Spring Boot, Redis, Docker and Docker Compose.

The service can use one of two method persistance, one that does not use any database solution. were the stocks are stored in a `ConcurrentHashMap` data structure, and another one were the stocks are stored in a Redis server configured with in-memory persistence only. 

The solution was containarized using Docker and compose files were provided to facilitate setup.

## Prerequisites

- [Docker](https://www.docker.com/)
- [Docker Compose](https://github.com/docker/compose)

__Note:__ To re-build the code, run the test suite or re-package the solution, [Java 8](https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html) is required. The service uses [Maven](https://maven.apache.org/) to build, run unit tests and create the target JAR file, but it is not necessary to have it installed, use the [Maven Wrapper](https://github.com/takari/maven-wrapper) binary present in project folder.

## Getting started

A docker compose file is provided to start the services and those environment variables can be used:

- __SERVICE_PORT__: The Stock service port
- __IN_MEM_PERSIST__: The persistence method used, it can be: `map`, were the stocks are stored in a `ConcurrentHashMap` data structure, and `redis`, were the stocks are stored in a Redis server configured with in-memory persistence only.

Execute the following steps:

1. Clone this repository

2. Run the following command on a terminal:

```sh
docker-compose up
```

Compose will download the necessary images from Docker Hub and start two containers , a Redis instance and a Spring Boot instance serving the content of this repository, provided as `./target/stocks-service-0.0.1-SNAPSHOT.jar` JAR file during the image building process.

The service will be up and running when printing a message similar to this:

```sh
2019-02-21 13:55:05.846  INFO 22321 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path '/api'
```

The service will be available in the port `8080`. (this port can be changed in Docker compose file or setting the `SERVICE_PORT` environment variable).

To run the service using the Hash Map persistence method, set the `IN_MEM_PERSIST` environment variable:

```sh
IN_MEM_PERSIST=map docker-compose up springboot
```

Sample stock data is loaded during the service startup, this information can be customized in `stockservice/application.yml` file.

The service provides the following endpoints:

### Usage

- `GET /api/stocks`: Get a list of stocks: `curl http://localhost:8080/api/stocks`
- `GET /api/stocks/{id}`: Get one stock from the list: `curl http://localhost:8080/api/stocks/1`
- `PUT /api/stocks/{id}`: Update the price of a single stock using the given payload information: `curl -X PUT --header "Content-Type: application/json" -d "{ \"currentPrice\": 88.0 }" http://localhost:8080/api/stocks/1`
- `POST /api/stocks`: Create a stock using the given payload information: `curl -X POST --header "Content-Type: application/json" -d "{ \"currentPrice\": 10.0, \"name\": \"New Stock\" }" http://localhost:8080/api/stocks`

The full API documentation is available at `/api/swagger-ui.html`.

### Unit tests

During the package creation, all unit test are executed. To re-executed them, go to `stockservice` folder and execute the following command:

```sh
./mvnw test
```

The test files are located under `stockservice/src/test` folder.

### Integration tests

1. The integration tests needs a Redis instance running locally. Redis container can be used, run the following command to start the container:

```sh
docker-compose up redis
```

2. Get the assigned Redis instance port:
 
```sh
docker ps
```

3. Go to `stocksservice` folder and run the tests making sure to provide the right connection information:

```sh
cd stocksservice
DB_HOST=localhost DB_PORT=<PORT> ./mvnw integration-test
```

## Building

Another compose file is provided to facilitate the image building process (`docker-compose.build.yml build --no-cache`), go to `stocksservice` folder and run the following command to re-build and generate a new JAR file:

```sh
stocksservice
./mvnw clean package
```

A new `stocksservice/target/stocks-service-service-1.0-SNAPSHOT.jar` will be created. After that the `springboot` image needs to be recreated.

1. Stop and destroy the current containers, first:

```sh
docker-compose down
```

2. Rebuild the images:

```sh
docker-compose -f docker-compose.build.yml build --no-cache
```

3. Start the containers:

```sh
docker-compose -f docker-compose.build.yml up
```
