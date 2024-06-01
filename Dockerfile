#Build
#FROM maven:3.8.4-openjdk-17-slim AS build
#WORKDIR /app
#COPY ecommerce-app /app/ecommerce-app
#RUN mvn package -f /app/ecommerce-app/pom.xml
#
##multi-staging
#FROM openjdk:17-slim
#WORKDIR /app
#COPY --from=build /app/ecommerce-app/target/ecommerce-app-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8088
#CMD ["java","-jar","app.jar"]

#docker build -t shopapp-spring:1.0.4 -f ./DockerfileJavaSpring .
#docker login
#create sunlight4d/shopapp-spring:1.0.4 repository on DockerHub
#docker tag shopapp-spring:1.0.4 sunlight4d/shopapp-spring:1.0.4
#docker push sunlight4d/shopapp-spring:1.0.4

# Which "official Java image"/ Ke thua tu image nao
FROM openjdk:17-slim
# Working directory/ Thu muc lam viec
WORKDIR /app
# Copy from host(Laptop, PC dev) to container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
# Run this inside image
RUN ./mvnw dependency:go-offline
CMD ["./mvnw", "spring-boot:run"]
