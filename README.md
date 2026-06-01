# Socks Storage

**Socks Storage** — это REST API на Spring Boot для управления складскими остатками носков на фабрике. Приложение предоставляет полный функционал CRUD-операций с партией носков, поддерживает фильтрацию, пагинацию, загрузку данных из Excel-файлов, а также аутентификацию и авторизацию с помощью JWT-токенов.

---

## Содержание

- [Стек технологий](#стек-технологий)
- [Возможности](#возможности)
- [API Endpoints](#api-endpoints)
  - [Авторизация и аутентификация](#авторизация-и-аутентификация)
  - [Работа с носками](#работа-с-носками)
- [Запуск приложения](#запуск-приложения)
  - [Локальный запуск](#локальный-запуск)
  - [Запуск через Docker Compose](#запуск-через-docker-compose)
- [Сборка проекта](#сборка-проекта)
- [Swagger UI](#swagger-ui)
- [Структура проекта](#структура-проекта)
- [Автор](#автор)

---

## Стек технологий

| Технология                  | Назначение                    |
|-----------------------------|-------------------------------|
| **Java 17**                 | Язык программирования         |
| **Spring Boot 3.5.6**       | Фреймворк                     |
| **Spring Web**              | REST API                      |
| **Spring Data JPA**         | Работа с базой данных         |
| **Spring Security**         | Аутентификация и авторизация  |
| **PostgreSQL**              | База данных                   |
| **JWT (jjwt)**              | Токены доступа                |
| **MapStruct**               | Маппинг DTO ↔ Entity         |
| **Apache POI**              | Чтение Excel-файлов           |
| **Swagger/OpenAPI**         | Документация API              |
| **Lombok**                  | Уменьшение шаблонного кода    |
| **Testcontainers**          | Интеграционное тестирование   |
| **Docker**                  | Контейнеризация               |

---

## Возможности

- ✅ **Регистрация и аутентификация пользователей** (JWT, роли `USER` / `ADMIN`)
- ✅ **Добавление носков на склад** (приходная операция)
- ✅ **Списание носков со склада** (расходная операция)
- ✅ **Обновление информации о партии носков**
- ✅ **Получение списка носков с фильтрацией и пагинацией**
- ✅ **Массовая загрузка носков из Excel-файла** (`.xlsx`)
- ✅ **Валидация входных данных**
- ✅ **Обработка ошибок с понятными HTTP-статусами**
- ✅ **Документация API через Swagger UI**
- ✅ **Контейнеризация с помощью Docker**

---

## API Endpoints

### Авторизация и аутентификация

| Метод   | Endpoint                | Описание                       | Доступ        |
|---------|-------------------------|--------------------------------|---------------|
| `POST`  | `/api/v1/auth/register` | Регистрация нового пользователя | ❌ Без JWT   |
| `GET`   | `/api/v1/auth/login`    | Вход и получение JWT-токена     | ❌ Без JWT   |

**Пример тела запроса `/register`:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123",
  "roles": ["USER"]
}
```

**Пример тела запроса `/login`:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

**Ответ:**
```json
{
  "token": "eyJhbGciOiJIUzI1...",
  "expirationDate": "2025-01-15T12:30:00"
}
```

---

### Работа с носками

Все эндпоинты (кроме `/api/v1/auth/**`) требуют JWT-токен в заголовке `Authorization: Bearer <token>`.

| Метод   | Endpoint                       | Описание                                     |
|---------|--------------------------------|----------------------------------------------|
| `GET`   | `/api/v1/socks`                | Получить список носков (фильтрация + пагинация) |
| `POST`  | `/api/v1/socks/income`         | Добавить носки на склад (приход)             |
| `POST`  | `/api/v1/socks/outcome`        | Списать носки со склада (расход)             |
| `PUT`   | `/api/v1/socks/{id}`           | Обновить информацию о партии носков          |
| `POST`  | `/api/v1/socks/batch`          | Загрузить носки из Excel-файла               |

#### GET `/api/v1/socks` — Параметры фильтрации

| Параметр         | Тип    | Описание                                |
|------------------|--------|-----------------------------------------|
| `color`          | String | Фильтр по цвету (без учёта регистра)    |
| `moreThanCount`  | Int    | Носков больше или равно указанному      |
| `lessThanCount`  | Int    | Носков меньше или равно указанному      |
| `moreThanCotton` | Int    | Процент хлопка больше или равно         |
| `lessThanCotton` | Int    | Процент хлопка меньше или равно         |
| `page`           | Int    | Номер страницы (для пагинации)          |
| `size`           | Int    | Количество элементов на странице        |

**Пример запроса:**
```
GET /api/v1/socks?color=red&moreThanCotton=50&lessThanCount=100&page=0&size=10
```

**Пример ответа:**
```json
{
  "content": [
    {
      "id": 1,
      "color": "red",
      "cottonPercentage": 80,
      "count": 50
    }
  ],
  "pageNumber": 0,
  "elementsPerPage": 10,
  "countPages": 1,
  "countElements": 1
}
```

#### POST `/api/v1/socks/income` — Приход носков
```json
{
  "color": "black",
  "cottonPercentage": 75,
  "count": 100
}
```

#### POST `/api/v1/socks/outcome` — Расход носков
> Если на складе недостаточно носков — вернётся `204 No Content`.
```json
{
  "color": "black",
  "cottonPercentage": 75,
  "count": 30
}
```

#### POST `/api/v1/socks/batch` — Загрузка из Excel

Файл формата `.xlsx` с колонками:
- **A** — Цвет (строка)
- **B** — Процент хлопка (число)
- **C** — Количество (число)

Первая строка считается заголовком и пропускается.

---

## Запуск приложения

### Локальный запуск

1. Убедитесь, что у вас установлены **Java 17** и **PostgreSQL**.
2. Настройте подключение к базе в `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/socks
    username: myuser
    password: mypassword
```

3. Соберите и запустите приложение:

```shell
./gradlew bootRun
```

### Запуск через Docker Compose

1. Убедитесь, что установлены **Docker** и **Docker Compose**.
2. Соберите jar-файл:

```shell
./gradlew bootJar
```

3. Запустите сервисы:

```shell
docker-compose up -d --build
```

Сервисы, которые будут запущены:
- **PostgreSQL** на порту `5433`
- **Приложение** на порту `8080`

Полезные команды:

```shell
# Остановка сервисов
docker-compose down

# Перезапуск с пересборкой
docker-compose up -d --build

# Войти в контейнер с БД
docker exec -it postgres_db /bin/bash
```

---

## Сборка проекта

```shell
# Сборка проекта (jar-файл)
./gradlew bootJar

# Запуск тестов
./gradlew test

# Публикация в локальный Maven-репозиторий
./gradlew publishToMavenLocal
```

Собранный jar будет находиться по пути: `build/libs/socks-1.0.jar`

---

## Swagger UI

После запуска приложения документация доступна по адресу:
```
http://localhost:8080/swagger-ui/index.html
```

Также можно получить OpenAPI-спецификацию:
```
http://localhost:8080/v3/api-docs
```

---

## Структура проекта

```
src/
├── main/
│   ├── java/org/example/socks/
│   │   ├── configuration/         # Конфигурации (Security, OpenAPI, JWT-фильтр)
│   │   ├── controller/            # REST-контроллеры (Auth, Socks)
│   │   ├── dto/                   # DTO (Socks, Auth, Filter, Exception)
│   │   ├── exception/             # Обработчики ошибок
│   │   ├── mapper/                # MapStruct-мапперы
│   │   ├── model/                 # Сущности БД (Socks, User, Role)
│   │   ├── repository/            # Spring Data JPA репозитории
│   │   ├── service/               # Бизнес-логика (интерфейсы и реализации)
│   │   ├── util/                  # Утилиты (Excel, спецификации для фильтрации)
│   │   └── SocksStorageApplication.java  # Точка входа
│   └── resources/
│       └── application.yml        # Конфигурация приложения
└── test/
    └── java/org/example/socks/    # Тесты (unit, integration)
```

---

## Автор

**Vladislav** — [GitHub](https://github.com/coldnsad)
