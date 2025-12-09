FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
ENV TZ=America/Santiago

COPY Wallet_DQXABCOJF1X64NFC /app/wallet
COPY --from=build /app/target/ms-ordenes-0.0.1-SNAPSHOT.jar app.jar

ENV KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
    KAFKA_TOPIC_PAGO_PENDIENTE=pago-pendiente \
    KAFKA_GROUP_ID=ms-ordenes-pagos-grp \
    TNS_ADMIN=/app/wallet

ENTRYPOINT ["java","-jar","/app/app.jar"]
