# sgf-fullstack-duoc

Proyecto semestral basado en el problema: **Sistema de Gestión Fronterizo**.
## Integrantes

- Sebastián Monsalve
- Miguel Pasten

## Descripción del proyecto

El Sistema de Gestión Fronterizo es una solución desarrollada con arquitectura de microservicios para apoyar procesos de control en pasos fronterizos.

El sistema permite gestionar usuarios, documentos de identidad, solicitudes de visa, declaraciones sanitarias, controles fronterizos, autenticación, logística, alertas y auditoría.

El objetivo principal es automatizar el flujo de revisión fronteriza, permitiendo que distintos microservicios se comuniquen entre sí para validar la información necesaria antes de autorizar un control fronterizo.

## Funcionalidades implementadas

| Microservicio | Funcionalidad | Puerto |
|---|---|---|
| ms-eurekaserver | Registro de microservicios activos | 8761 |
| ms-gateway | Punto de entrada central del sistema | 8080 |
| ms-auth | Autenticación y autorización de usuarios | 8081 |
| ms-logistics | Gestión logística del proceso fronterizo | 8082 |
| ms-alerts | Gestión de alertas del sistema | 8083 |
| ms-audit | Registro de auditorías y trazabilidad | 8084 |
| ms-users | Gestión de usuarios del sistema | 8089 |
| ms-identity | Gestión y validación de documentos de identidad | 8087 |
| ms-visa | Gestión de solicitudes de visa | 8086 |
| ms-health | Gestión de declaraciones sanitarias | 8091 |
| ms-bordercontrol | Validación final y control fronterizo | 8092 |

## Flujo principal del sistema

El flujo principal del sistema es:

1. Se registra un usuario en `ms-users`.
2. Se registra y valida un documento de identidad en `ms-identity`.
3. Se crea y aprueba una solicitud de visa en `ms-visa`.
4. Se crea y evalúa una declaración sanitaria en `ms-health`.
5. `ms-bordercontrol` consulta los microservicios anteriores y autoriza o rechaza el control fronterizo.

`ms-bordercontrol` consume información de otros microservicios mediante OpenFeign para validar:

- Usuario activo.
- Documento de identidad validado.
- Visa aprobada.
- Declaración sanitaria apta.

Si todas las condiciones se cumplen, el control fronterizo queda en estado `AUTORIZADO`.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Oracle Database
- MySQL
- Docker
- Lombok
- Bean Validation
- OpenFeign
- Eureka Server
- Spring Cloud Gateway
- Kafka
- Postman
- GitHub

## Bases de datos usadas

El proyecto utiliza bases de datos levantadas mediante Docker.

| Contenedor | Motor | Puerto local | Uso |
|---|---|---|---|
| oracle-free | Oracle Free | 1521 | ms-users, ms-identity, ms-visa, ms-bordercontrol, ms-auth, ms-audit |
| mysql-health | MySQL 8.4 | 3306 | ms-health |
| mysql-logistics | MySQL 8.0 | 3307 | ms-logistics y ms-alerts |

## Pasos para ejecutar el proyecto

### 1. Abrir Docker Desktop

Antes de iniciar los microservicios, se debe abrir Docker Desktop y verificar que esté funcionando.

Para revisar los contenedores activos:

```powershell
docker ps
```

### 2. Iniciar las bases de datos

Iniciar Oracle:

```powershell
docker start oracle-free
```

Iniciar MySQL Health:

```powershell
docker start mysql-health
```

Iniciar MySQL Logistics:

```powershell
docker start mysql-logistics
```

Si los contenedores no existen, se pueden crear con los siguientes comandos:

```powershell
docker run --name oracle-free -e ORACLE_PASSWORD="Admin12345*" -p 1521:1521 -d gvenzl/oracle-free:23-slim
```

```powershell
docker run --name mysql-health -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=ms_health -e MYSQL_USER=MS_HEALTH -e MYSQL_PASSWORD=1234 -p 3306:3306 -d mysql:8.4
```

```powershell
docker run --name mysql-logistics -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=ms_logistics -p 3307:3306 -d mysql:8.0
```

### 3. Iniciar los microservicios

El orden recomendado para iniciar el sistema es:

```text
1. MsEurekaserverApplication   → 8761
2. MsAuthApplication           → 8081
3. MsLogisticsApplication      → 8082
4. MsAlertsApplication         → 8083
5. MsAuditApplication          → 8084
6. MsUsersApplication          → 8089
7. MsIdentityApplication       → 8087
8. MsVisaApplication           → 8086
9. MsHealthApplication         → 8091
10. MsBorderControlApplication → 8092
11. MsGatewayApplication       → 8080
```

El Gateway debe iniciarse al final, porque funciona como punto de entrada hacia los demás microservicios.

### 4. Verificar Eureka

Una vez iniciado `ms-eurekaserver`, se puede revisar en el navegador:

```text
http://localhost:8761
```

En esa página deben aparecer los microservicios registrados con estado `UP`.

### 5. Probar el Gateway

Cuando todos los microservicios estén activos, se pueden probar los endpoints desde el Gateway:

```http
GET http://localhost:8080/api/v1/users
GET http://localhost:8080/api/v1/identity-documents
GET http://localhost:8080/api/v1/visa-requests
GET http://localhost:8080/api/v1/health-declarations
GET http://localhost:8080/api/v1/border-controls
GET http://localhost:8080/api/v1/alerts
GET http://localhost:8080/api/v1/logistics
```

## Endpoints principales del flujo

### Crear usuario

```http
POST http://localhost:8080/api/v1/users
```

### Crear documento de identidad

```http
POST http://localhost:8080/api/v1/identity-documents
```

### Validar documento

```http
PATCH http://localhost:8080/api/v1/identity-documents/{id}/validate
```

### Crear solicitud de visa

```http
POST http://localhost:8080/api/v1/visa-requests
```

### Aprobar visa

```http
PATCH http://localhost:8080/api/v1/visa-requests/{id}/approve
```

### Crear declaración sanitaria

```http
POST http://localhost:8080/api/v1/health-declarations
```

### Evaluar declaración sanitaria

```http
PATCH http://localhost:8080/api/v1/health-declarations/{id}/evaluate
```

### Crear control fronterizo

```http
POST http://localhost:8080/api/v1/border-controls
```

## Migraciones y scripts SQL

El proyecto incluye scripts SQL iniciales para documentar la estructura de base de datos de los microservicios.

Durante la ejecución local se utiliza:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.flyway.enabled=false
```

Esto permite que Hibernate actualice las tablas automáticamente durante las pruebas locales.

## Control de versiones

El proyecto utiliza GitHub como herramienta de control de versiones.

Los commits se realizaron con mensajes técnicos, separando cambios de configuración, documentación, scripts SQL, controladores, servicios, DTOs y comunicación entre microservicios.