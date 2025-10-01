# Lab1 Infobez

Лабораторная работа по информационной безопасности.  
Реализовано простое REST API на **Spring Boot** с базовыми мерами защиты: аутентификация через JWT, защита от SQL-инъекций и XSS.

---

## API эндпоинты

###  Аутентификация
- **POST** `/auth/register`  
  Регистрация нового пользователя.  
  **Параметры (JSON):**
  ```json
  {
    "username": "testuser",
    "password": "mypassword"
  }

Ответ:

{ "message": "registered" }


- **POST** /auth/login
Логин пользователя. Возвращает JWT токен.
Параметры (JSON):
  ```json
  {
  "username": "testuser",
  "password": "mypassword"
  }


Ответ:

{
"type": "Bearer",
"token": "eyJhbGciOiJIUzI1NiJ9..."
}

###  Работа с данными

- ** GET** /api/data
Получение списка данных (только для аутентифицированных пользователей).
Пример ответа:

[
{
"id": 1,
"title": "Hello",
"owner": "testuser"
},
{
"id": 2,
"title": "World",
"owner": "testuser"
}
]


### Реализованные меры безопасности
### Защита от SQL-инъекций

Используется Spring Data JPA (Hibernate).

Все запросы выполняются через ORM и параметризованные PreparedStatement.

Конкатенация строк в SQL-запросах отсутствует.


###  Защита от XSS

Все данные, возвращаемые в API, проходят сериализацию через Jackson.

Пользовательский ввод при необходимости экранируется (HtmlUtils.htmlEscape()).


###  Защита от "Broken Authentication"

Реализована JWT-аутентификация:

При успешном логине генерируется токен с подписью.

Все защищённые эндпоинты проверяют токен через фильтр JwtAuthFilter.

Пароли пользователей никогда не хранятся в открытом виде:

Используется алгоритм BCrypt с фактором сложности 12.

Доступ к данным разрешён только аутентифицированным пользователям.

###  CI/CD и проверка безопасности

Настроен GitHub Actions (.github/workflows/ci.yml).

При каждом push или pull request запускаются проверки:




### Технологии

Java 17

Spring Boot 3.x

Spring Security

PostgreSQL

JPA/Hibernate

JWT

BCrypt

###  Запуск проекта локально
1. Настроить базу данных PostgreSQL
   CREATE DATABASE infobez_db;
   CREATE USER infobez_user WITH ENCRYPTED PASSWORD 'strongpassword123';
   GRANT ALL PRIVILEGES ON DATABASE infobez_db TO infobez_user;

2. Настроить application.properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/infobez_db
   spring.datasource.username=infobez_user
   spring.datasource.password=strongpassword123
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

security.jwt.secret=super-secret-key-change-me
security.jwt.expirationMinutes=60

3. Собрать и запустить
   mvn clean install
   mvn spring-boot:run


Приложение будет доступно на http://localhost:8082

###  Автор

Ксения Виданович