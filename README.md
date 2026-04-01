# Sistema de Domésticas

Plataforma para gestionar tareas domésticas en hogares compartidos utilizando arquitectura hexagonal (Hexagonal Architecture).

## Descripción del Proyecto

Esta aplicación permite organizar y distribuir tareas domésticas en hogares compartidos, facilitando la asignación, seguimiento y cumplimiento de responsabilidades entre los miembros del hogar.

### Características Principales

- **Registro de usuarios y grupos familiares**
- **Creación y asignación de tareas domésticas**
- **Definición de fechas límite y prioridades**
- **Registro del estado de cada tarea**
- **Historial de cumplimiento por usuario**
- **Reportes sobre distribución de responsabilidades**

## Arquitectura

El proyecto sigue los principios de **Hexagonal Architecture**, separando las preocupaciones en capas claramente definidas:

- **Domain**: Entidades de negocio, reglas de negocio y interfaces de repositorio
- **Application**: Casos de uso y servicios de aplicación
- **Infrastructure**: Implementaciones de repositorio y adaptadores externos
- **Presentation**: Controladores REST y DTOs

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**

## Configuración

1. **Base de Datos**: Configurar PostgreSQL y actualizar `application.properties` con las credenciales correctas.

2. **Dependencias**: Ejecutar `mvn install` para instalar las dependencias.

3. **Ejecución**: Ejecutar `mvn spring-boot:run` para iniciar la aplicación.

## Endpoints API

### Usuarios
- `POST /api/users` - Crear usuario
- `GET /api/users/{id}` - Obtener usuario por ID
- `GET /api/users` - Obtener todos los usuarios
- `PUT /api/users/{id}` - Actualizar usuario
- `DELETE /api/users/{id}` - Eliminar usuario

### Grupos
- `POST /api/hogares` - Crear grupo
- `GET /api/hogares/{id}` - Obtener grupo por ID
- `GET /api/hogares` - Obtener todos los grupos
- `PUT /api/hogares/{id}` - Actualizar grupo
- `DELETE /api/hogares/{id}` - Eliminar grupo

### Tareas
- `POST /api/tasks` - Crear tarea
- `GET /api/tasks/{id}` - Obtener tarea por ID
- `GET /api/tasks/group/{groupId}` - Obtener tareas por grupo
- `GET /api/tasks/user/{userId}` - Obtener tareas por usuario
- `PUT /api/tasks/{id}` - Actualizar tarea
- `PATCH /api/tasks/{id}/status` - Actualizar estado de tarea
- `DELETE /api/tasks/{id}` - Eliminar tarea

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/domesticas/
│   │       ├── SistemaDomesticasApplication.java
│   │       ├── domain/
│   │       │   ├── User.java
│   │       │   ├── Hogar.java
│   │       │   ├── Task.java
│   │       │   ├── UserRepository.java
│   │       │   ├── HogarRepository.java
│   │       │   └── TaskRepository.java
│   │       ├── application/
│   │       │   ├── UserService.java
│   │       │   ├── CrearHogarService.java
│   │       │   └── TaskService.java
│   │       ├── infrastructure/
│   │       │   ├── JpaUserRepository.java
│   │       │   ├── JpaGroupRepository.java
│   │       │   └── JpaTaskRepository.java
│   │       └── presentation/
│   │           ├── UserController.java
│   │           ├── HogarController.java
│   │           └── TaskController.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/domesticas/
```

## Próximos Pasos

- Implementar autenticación y autorización
- Agregar frontend web
- Implementar reportes y estadísticas
- Agregar notificaciones
- Tests unitarios e integración
