# Используйте официальный образ Java как базовый образ
FROM openjdk:17

# Укажите рабочую директорию внутри образа
WORKDIR /app

# Скопируйте собранный jar-файл в рабочую директорию
COPY ./target/DailyDutyBot-1.0-SNAPSHOT.jar /app/

# Укажите команду для запуска вашего приложения
CMD ["java", "-jar", "DailyDutyBot-1.0-SNAPSHOT.jar"]

# Откройте порт, который использует ваше приложение, если это веб-приложение
EXPOSE 8080

#FROM openjdk:19-jdk-slim
#
## Установка Maven
#RUN apt-get update && \
#    apt-get install -y maven && \
#    rm -rf /var/lib/apt/lists/*
#
#WORKDIR /app
#
## Копирование pom.xml для загрузки зависимостей
#COPY pom.xml .
#
## Копирование исходников для сборки
#COPY src ./src
#
## Сборка JAR файла
#RUN mvn -f pom.xml clean package
#
## Копирование JAR файла после сборки
#COPY target/DailyDutyBot-1.0-SNAPSHOT.jar app.jar
#
#CMD ["java", "-jar", "app.jar"]
