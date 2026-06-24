# Sistema de Gestión Fronteriza (SGF)

## Integrantes

* Sebastián Monsalve
* Miguel Pasten

---

# Descripción del Proyecto

El Sistema de Gestión Fronteriza (SGF) es una plataforma basada en arquitectura de microservicios orientada a la gestión y control de procesos fronterizos. La solución permite administrar usuarios, documentos de identidad, solicitudes de visa, declaraciones de salud, logística, control fronterizo, auditoría y alertas, utilizando una arquitectura distribuida y escalable.

El proyecto fue desarrollado utilizando Spring Boot, Spring Cloud, Oracle Database, Kafka, Docker y API Gateway.

---

# Arquitectura de Microservicios

El sistema está compuesto por los siguientes microservicios:

## Comunicación

* ms-eurekaserver
* ms-gateway

## Seguridad

* ms-auth
* ms-audit
* ms-alerts

## Negocio

* ms-users
* ms-identity
* ms-visa
* ms-logistics
* ms-health
* ms-bordercontrol

---

# Tecnologías Utilizadas

* Java 21
* Spring Boot
* Spring Cloud Gateway
* Eureka Server
* OpenFeign
* Oracle Database
* Apache Kafka
* Maven
* Docker
* Swagger OpenAPI
* GitHub
* Trello

---

# Comunicación entre Microservicios

La comunicación entre servicios se realiza mediante OpenFeign, permitiendo el intercambio de información entre los distintos dominios del sistema.

Ejemplos:

* Identity consulta usuarios en Users.
* Visa consulta documentos de identidad.
* BorderControl consulta información de salud y visas.
* Audit registra eventos generados por otros servicios.

---

# API Gateway

El sistema utiliza Spring Cloud Gateway para centralizar el acceso a los microservicios.

Puerto:

```text
9090
```

Rutas principales:

```text
/api/users/**
/api/identity/**
/api/visa/**
/api/logistics/**
/api/health/**
/api/bordercontrol/**
/api/auth/**
/api/audit/**
/api/alerts/**
```

---

# Documentación Swagger

Swagger se encuentra habilitado en los microservicios para la exploración y prueba de endpoints.

Ejemplo local:

```text
http://localhost:8081/swagger-ui.html
http://localhost:8082/swagger-ui.html
http://localhost:8083/swagger-ui.html
```

---

# Ejecución del Proyecto

## Requisitos

* Docker Desktop
* Java 21
* Maven
* Git

## Clonar repositorio

```bash
git clone <url-del-repositorio>
```

## Levantar el proyecto

Desde la raíz del proyecto:

```bash
docker compose up --build -d
```

## Verificar contenedores

```bash
docker ps
```

## Detener servicios

```bash
docker compose down
```

---

# Base de Datos

El sistema utiliza Oracle Database ejecutándose mediante Docker.

Puerto Oracle:

```text
1555
```

---

# Herramientas de Gestión

El proyecto fue gestionado utilizando:

* GitHub para control de versiones.
* Trello para planificación y seguimiento de tareas.
* Postman para pruebas de endpoints.

---

# Características Implementadas

* Arquitectura basada en microservicios.
* API Gateway centralizado.
* Descubrimiento de servicios mediante Eureka.
* Comunicación entre servicios utilizando OpenFeign.
* Persistencia de datos con Oracle Database.
* Mensajería asíncrona mediante Kafka.
* Contenerización utilizando Docker.
* Documentación mediante Swagger/OpenAPI.
* Logging para monitoreo y trazabilidad de operaciones.

---

# Estado del Proyecto

Proyecto funcional desarrollado como parte de la asignatura Desarrollo FullStack 1, implementando una arquitectura distribuida basada en microservicios y buenas prácticas de desarrollo de software.
