FROM adoptopenjdk/openjdk11
EXPOSE 4005
COPY target/paymentService-0.0.1-SNAPSHOT.jar payment-service-0.0.1.jar
ENTRYPOINT ["java","-jar","/payment-service-0.0.1.jar"]