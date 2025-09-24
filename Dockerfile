# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# Рабочая директория
WORKDIR /app

# Копируем jar в контейнер
COPY build/libs/socks-1.0.jar app.jar

# Запуск приложения
ENTRYPOINT ["java","-jar","app.jar"]
