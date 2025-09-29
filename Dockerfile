FROM openjdk:21
MAINTAINER jw
COPY target/cart-service-0.0.1-SNAPSHOT.jar cart-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/cart-service-0.0.1-SNAPSHOT.jar"]