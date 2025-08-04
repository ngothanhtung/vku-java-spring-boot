# Dockerfile for VKU Java Spring Boot Application

## Install Docker Desktop
Before you begin, ensure that you have Docker Desktop installed on your machine. You can download it from the official Docker website: [Docker Desktop](https://www.docker.com/products/docker-desktop).
## Dockerfile Overview
The `Dockerfile` provided in this directory is designed to create a Docker image for the VKU Java Spring Boot application. It uses the official OpenJDK 21 image as the base image, sets the working directory, copies the application JAR file into the image, and specifies the command to run the application.


This Dockerfile builds a Docker image for the VKU Java Spring Boot application.

## Login to Docker Hub
Before pushing the Docker image to Docker Hub, you need to log in to your Docker Hub account. Use the following command to log in:
```bash
docker login
```
You will be prompted to enter your Docker Hub username and password.

## Build the Docker Image
To build the Docker image, navigate to the directory containing the `Dockerfile` and run the following command:

```bash
docker build -t thanhtungo/vku-java-spring-boot-demo:latest .
```

## Push the Docker Image to Docker Hub
After building the Docker image, you can push it to Docker Hub using the following command:

```bash  
docker push thanhtungo/vku-java-spring-boot-demo:latest

```

## Pull the Docker Image from Docker Hub
If you want to pull the Docker image from Docker Hub, you can use the following command:
```bash
docker pull thanhtungo/vku-java-spring-boot-demo:latest
```

## Run with Docker Compose
To run the VKU Java Spring Boot application using Docker Compose, you can use the following command. Make sure you have Docker Compose installed and the `docker-compose.yml` file is correctly configured.

```bash
docker compose -p vku-java-spring-boot-demo up
```
## Stop and Remove Containers
To stop and remove the containers created by Docker Compose, use the following command:
```bash
docker compose -p vku-java-spring-boot-demo down
```

## Stop and Remove Containers with Volumes
To stop and remove the containers along with their associated volumes, use the following command:
```
docker-compose down -v
```