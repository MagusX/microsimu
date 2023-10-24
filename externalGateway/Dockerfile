FROM adoptopenjdk/openjdk11
EXPOSE 4001
COPY target/externalGateway-0.0.1-SNAPSHOT.jar external-gateway-service-0.0.1.jar
ENTRYPOINT ["java","-jar","/external-gateway-service-0.0.1.jar"]