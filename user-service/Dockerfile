# Используем базовый образ OpenJDK
FROM openjdk:21-jdk-slim

# Указываем рабочую директорию
WORKDIR /app

# Копируем jar-файл (убедитесь, что jar файл генерируется в этом пути)
COPY target/user-service.jar user-service.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "user-service.jar"]