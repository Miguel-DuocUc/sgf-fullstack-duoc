# sgf-fullstack-duoc

Proyecto semestral basado en el problema: Sistema de Gestión Fronterizo.

# Integrantes

* Sebastián Monsalve
* Miguel Pasten

# Descripción del proyecto

El Sistema de Gestión Fronterizo es una solución desarrollada bajo una arquitectura de microservicios utilizando Spring Boot y Spring Cloud.

El sistema permite gestionar usuarios, documentos de identidad, solicitudes de visa, declaraciones sanitarias, controles fronterizos, autenticación, logística, alertas y auditoría.

El objetivo principal es automatizar el proceso de validación fronteriza mediante servicios independientes que se comunican entre sí para verificar la información necesaria antes de autorizar el ingreso de una persona.

# Funcionalidades implementadas

| Microservicio    | Funcionalidad                                   |
| ---------------- | ----------------------------------------------- |
| ms-eurekaserver  | Registro y descubrimiento de microservicios     |
| ms-gateway       | Punto de entrada central del sistema            |
| ms-auth          | Autenticación y autorización                    |
| ms-logistics     | Gestión logística del proceso fronterizo        |
| ms-users         | Gestión de usuarios                             |
| ms-bordercontrol | Validación final y control fronterizo           |
| ms-visa          | Gestión de solicitudes de visa                  |
| ms-identity      | Gestión y validación de documentos de identidad |
| ms-health        | Gestión de declaraciones sanitarias             |
| ms-alerts        | Gestión de alertas                              |
| ms-audit         | Registro de auditorías y trazabilidad           |

# Flujo principal del sistema

El flujo principal del sistema es:

1. Se registra un usuario en ms-users.
2. Se registra y valida un documento de identidad en ms-identity.
3. Se crea y aprueba una solicitud de visa en ms-visa.
4. Se registra una declaración sanitaria en ms-health.
5. ms-bordercontrol consulta los microservicios anteriores y determina si el control fronterizo puede ser autorizado.
6. Si todas las validaciones son correctas, el control fronterizo queda autorizado.

ms-bordercontrol consume información de otros microservicios mediante OpenFeign para validar:

* Usuario activo.
* Documento de identidad validado.
* Visa aprobada.
* Declaración sanitaria apta.

# Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Spring Security
* Spring Cloud Gateway
* Eureka Server
* OpenFeign
* Apache Kafka
* Oracle Database
* MySQL
* Flyway Migration
* Bean Validation (JSR-380)
* Lombok
* Maven
* Docker
* Docker Compose
* Postman
* GitHub

# Bases de datos usadas

El proyecto utiliza bases de datos ejecutadas mediante Docker.

| Motor           | Uso                                                                 |
| --------------- | ------------------------------------------------------------------- |
| Oracle Database | ms-users, ms-identity, ms-visa, ms-bordercontrol, ms-auth, ms-audit |
| MySQL           | ms-health                                                           |
| MySQL           | ms-logistics y ms-alerts                                            |

# Comunicación entre microservicios

La comunicación entre microservicios se realiza mediante OpenFeign.

Actualmente se implementan integraciones entre:

* ms-bordercontrol ↔ ms-users
* ms-bordercontrol ↔ ms-identity
* ms-bordercontrol ↔ ms-visa
* ms-bordercontrol ↔ ms-health

Además, se incorpora Apache Kafka para la publicación de eventos relacionados con el control fronterizo.

# Características técnicas implementadas

* Arquitectura basada en microservicios.
* Patrón CSR (Controller - Service - Repository).
* DTOs para comunicación entre capas.
* Persistencia mediante JPA/Hibernate.
* Migraciones SQL por microservicio.
* Manejo global de excepciones mediante ApiExceptionHandler.
* Validaciones utilizando Bean Validation.
* Seguridad mediante Spring Security.
* Comunicación distribuida mediante OpenFeign.
* Eventos asincrónicos mediante Kafka.
* Contenerización mediante Docker.
* Registro y descubrimiento de servicios mediante Eureka.

# Pasos para ejecutar el proyecto

## 1. Levantar Docker

Verificar que Docker Desktop se encuentre iniciado.

Comprobar funcionamiento:

```bash
docker ps
```

## 2. Levantar infraestructura

Desde la raíz del proyecto:

```bash
docker compose up -d
```

Verificar contenedores:

```bash
docker ps
```

## 3. Iniciar los microservicios

Orden recomendado:

1. ms-eurekaserver
2. ms-auth
3. ms-logistics
4. ms-alerts
5. ms-audit
6. ms-users
7. ms-identity
8. ms-visa
9. ms-health
10. ms-bordercontrol
11. ms-gateway

## 4. Verificar Eureka

Abrir:

```text
http://localhost:8761
```

Todos los microservicios deben aparecer en estado UP.

## 5. Probar Gateway

Ejemplos:

```http
GET /api/v1/users
GET /api/v1/identity-documents
GET /api/v1/visa-requests
GET /api/v1/health-declarations
GET /api/v1/border-controls
GET /api/v1/logistics
GET /api/v1/alerts
```

# Endpoints principales

## Usuarios

```http
POST /api/v1/users
GET /api/v1/users
PUT /api/v1/users/{id}
DELETE /api/v1/users/{id}
```

## Identidad

```http
POST /api/v1/identity-documents
PATCH /api/v1/identity-documents/{id}/validate
```

## Visa

```http
POST /api/v1/visa-requests
PATCH /api/v1/visa-requests/{id}/approve
```

## Salud

```http
POST /api/v1/health-declarations
PATCH /api/v1/health-declarations/{id}/evaluate
```

## Control Fronterizo

```http
POST /api/v1/border-controls
GET /api/v1/border-controls
```

# Migraciones y persistencia

Cada microservicio incorpora scripts de migración inicial para la creación de tablas y estructuras de datos.

Durante las pruebas locales se utiliza:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Las estructuras incluyen:

* Usuarios
* Documentos de identidad
* Solicitudes de visa
* Declaraciones sanitarias
* Controles fronterizos

# Control de versiones

El proyecto utiliza GitHub como herramienta de control de versiones.

Los commits fueron organizados por módulos y funcionalidades, documentando:

* Actualización de usuarios e identidad.
* Actualización de visas y declaraciones sanitarias.
* Actualización del flujo de control fronterizo.
* Integración mediante OpenFeign.
* Configuración Docker y despliegue.
* Mejoras de seguridad, validaciones y manejo de excepciones.

# Proyecto Académico

Proyecto desarrollado para la asignatura Desarrollo FullStack, aplicando arquitectura distribuida basada en microservicios, persistencia de datos, comunicación entre servicios, seguridad, Docker, Eureka, Gateway, OpenFeign y Kafka.
