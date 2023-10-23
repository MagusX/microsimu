FROM adoptopenjdk/openjdk11
EXPOSE 4000
COPY target/internalGateway-0.0.1-SNAPSHOT.jar internal-gateway-service-0.0.1.jar
ENTRYPOINT ["java","-jar","/internal-gateway-service-0.0.1.jar"]