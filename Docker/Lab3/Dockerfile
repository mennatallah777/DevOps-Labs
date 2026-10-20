FROM eclipse-temurin:17-jre

WORKDIR /app

RUN useradd -m appuser && chown appuser /app
USER appuser

COPY --chown=appuser target/calculator.jar .

EXPOSE 8080

CMD ["java", "-jar", "calculator.jar"]
