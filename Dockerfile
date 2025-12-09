# ===== Build =====
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests package


# ===== Run =====
FROM eclipse-temurin:21-jre
WORKDIR /app
ENV TZ=America/Santiago

# Copiar wallet completo (debe copiarse la carpeta con su contenido exacto)
COPY Wallet_DQXABCOJF1X64NFC /app/wallet

# Copiar la JAR generada
COPY --from=build /app/target/ms-ordenes-0.0.1-SNAPSHOT.jar app.jar


# Variables de Entorno (sobreescriben application.properties)
ENV KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
    KAFKA_GROUP_ID=ms-ordenes-pagos-grp


ENTRYPOINT ["java","-jar","/app/app.jar"]