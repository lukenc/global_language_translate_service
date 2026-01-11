# Build stage: 使用 Java 21 与 Maven 离线缓存
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package -DskipTests

# Runtime stage: 精简 JRE 运行时
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /workspace/target/web_content_translate-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]