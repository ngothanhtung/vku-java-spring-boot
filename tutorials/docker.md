# Dockerfile for VKU Java Spring Boot Application

This Dockerfile builds a Docker image for the VKU Java Spring Boot application.

## Build the Docker Image

```bash
docker build -t thanhtungo/vku-java-spring-boot:latest .
```

## Push the Docker Image to Docker Hub

After building the Docker image, you can push it to Docker Hub using the following command:

```bash  
docker push thanhtungo/vku-java-spring-boot:latest

```

## Run the Docker Container

To run the Docker container, use the following command:

```bash
docker run -d -p 8888:8888 --name vku-java-spring-boot thanhtungo/vku-java-spring-boot:latest
```
