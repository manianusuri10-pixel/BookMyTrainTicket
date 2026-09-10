# ---------- BUILD STAGE ----------
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests -B

# ---------- RUN STAGE ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

# Render passes the container port via $PORT, so command uses that
# and prints the same Java app logs to stdout/stderr for visibility.
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar --logging.level.root=INFO 2>&1"]
