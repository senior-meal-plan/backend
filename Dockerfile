FROM gradle:8.5.0-jdk17 AS builder
WORKDIR /app
COPY build.gradle settings.gradle .
COPY src ./src
RUN ./gradlew bootJar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 타임존 서울로 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]