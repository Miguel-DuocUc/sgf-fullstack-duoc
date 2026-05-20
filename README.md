# sgf-fullstack-duoc
Proyecto Semestral basado en el problema, Sistema de gestión fronterizo

## Integrantes
- Sebastián Monsalve
- Miguel


## Contexto del proyecto

El Sistema de Gestión Fronterizo es una solución basada en arquitectura de microservicios para apoyar procesos de control en pasos fronterizos. El sistema permite gestionar usuarios, documentos de identidad, solicitudes de visa, declaraciones sanitarias, controles fronterizos, autenticación, alertas, auditoría y logística.

El objetivo principal es automatizar y ordenar el flujo de revisión fronteriza, permitiendo que distintos microservicios se comuniquen entre sí para validar información antes de autorizar un control fronterizo.

## Microservicios implementados

| Microservicio | Descripción | Puerto |
|---|---|---|
| ms-gateway | Punto de entrada central del sistema | 8080 |
| ms-auth | Autenticación y autorización de usuarios | 8081 |
| ms-logistics | Gestión logística del proceso fronterizo | 8082 |
| ms-alerts | Gestión de alertas del sistema | 8083 |
| ms-audit | Registro de auditorías y trazabilidad | 8084 |
| ms-visa | Gestión de solicitudes de visa | 8086 |
| ms-identity | Gestión y validación de documentos de identidad | 8087 |
| ms-users | Gestión de usuarios del sistema | 8089 |
| ms-health | Gestión de declaraciones sanitarias | 8091 |
| ms-bordercontrol | Control fronterizo y validación final del viajero | 8092 |
| ms-eurekaserver | Registro de microservicios activos | 8761 |

## Bases de datos usadas

El proyecto utiliza bases de datos levantadas mediante Docker.

| Base de datos | Motor | Puerto local | Uso |
|---|---|---|---|
| oracle-sgf | Oracle XE | 1522 | ms-users, ms-identity, ms-visa, ms-bordercontrol, ms-auth, ms-audit |
| mysql-health | MySQL 8.4 | 3306 | ms-health, ms-alerts, ms-logistics |

## Tecnologías utilizadas

- Java
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
- Gateway
- Kafka
- Postman
- GitHub

## Comando para iniciar Docker

Primero se debe abrir Docker Desktop.

Luego, desde la raíz del proyecto, ejecutar:

```powershell
docker compose up -d