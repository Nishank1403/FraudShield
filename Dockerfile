FROM eclipse-temurin:21-jre

WORKDIR /app
COPY target/fraudshield.jar /app/fraudshield.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/fraudshield.jar"]
