# Используем базовый образ OpenJDK
FROM openjdk:17-jdk-slim

# Указываем рабочую директорию
WORKDIR /app

# Копируем jar-файл (убедитесь, что jar файл генерируется в этом пути)
COPY target/notification-service.jar notification-service.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "notification-service.jar"]