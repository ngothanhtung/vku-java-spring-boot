# Dockerfile for VKU Java Spring Boot Application

This Dockerfile builds a Docker image for the VKU Java Spring Boot application.

## Build the Docker Image

```bash
docker build -t thanhtungo/vku-java-spring-boot:latest -f docker/Dockerfile .
```

## Push the Docker Image to Docker Hub

After building the Docker image, you can push it to Docker Hub using the following command:

```bash  
docker push thanhtungo/vku-java-spring-boot:latest

```

## Run with Docker Compose

```bash
docker compose -p vku-java-spring-boot -f docker/docker-compose.yml up
```

```bash
docker compose -p vku-java-spring-boot -f docker/docker-compose.yml down
```
