# 📋 Talgham — Sistema de gestión para un estudio contable

[🇦🇷 Español](#-español) · [🇬🇧 English](#-english)

![Java](https://img.shields.io/badge/Java-8-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-1.5-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-BCrypt-6DB33F?logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/DB-MySQL-4479A1?logo=mysql&logoColor=white)
![Bootstrap](https://img.shields.io/badge/UI-Bootstrap-7952B3?logo=bootstrap&logoColor=white)

---

## 🇦🇷 Español

Aplicación web para la gestión de un **estudio contable**: organiza los pedidos de trabajo de los clientes desde que se reciben hasta que se terminan, con usuarios, perfiles, roles y avisos por email.

> 💼 **Proyecto real:** lo desarrollamos para un estudio contable y lo vendimos.

### Funcionalidades
- **Solicitudes** con ciclo de estados: *Solicitado → En proceso → Detenido → Finalizado / Cancelado*, y opción de programarlas.
- **Clientes, trabajos y programas** con alta, baja y modificación.
- **Agenda** de solicitudes.
- **Usuarios, perfiles y roles** (desarrollador, administrador, contador, cliente) con permisos.
- **Login seguro** con Spring Security y contraseñas cifradas con **BCrypt**.
- **Historial de actividades** de cada solicitud.
- **Notificaciones por email** con plantillas para clientes y contadores.

### Tecnologías
- **Java 8**, **Spring Boot 1.5**: Spring MVC, Spring Data JPA, Spring Security, Spring Mail, AOP
- **Mustache** para las vistas, **Bootstrap** y **Font Awesome** para la interfaz
- **MySQL** como base de datos
- **Gradle** para el build

### Arquitectura
Aplicación en capas:
```
src/main/java/com/talgham/demo
├── controller/   # Controladores MVC (solicitudes, clientes, usuarios, roles...)
├── service/      # Lógica de negocio
├── repository/   # Acceso a datos con Spring Data JPA
├── model/        # Entidades del dominio
├── auth/         # Autenticación (UserDetailsService)
└── common/       # Constantes (estados, perfiles, roles)
```

### Cómo ejecutarlo
1. Creá una base de datos MySQL.
2. Creá el archivo `src/main/resources/application.properties` con tu conexión:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/talgham
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_PASSWORD
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Iniciá la aplicación:
   ```bash
   ./gradlew bootRun      # en Windows: gradlew.bat bootRun
   ```
4. Abrí `http://localhost:8080`.

---

## 🇬🇧 English

Web application for managing an **accounting firm**: it tracks client work requests from intake to completion, with users, profiles, roles and email notifications.

> 💼 **Real-world project:** built for an accounting firm and sold to them.

### Features
- **Requests** with a status workflow: *Requested → In progress → On hold → Done / Cancelled*, plus scheduling.
- **Clients, jobs and programs**: full CRUD.
- **Calendar** view of requests.
- **Users, profiles and roles** (developer, admin, accountant, client) with permissions.
- **Secure login** with Spring Security and **BCrypt**-hashed passwords.
- **Activity history** for each request.
- **Email notifications** using templates for clients and accountants.

### Tech stack
- **Java 8**, **Spring Boot 1.5**: Spring MVC, Spring Data JPA, Spring Security, Spring Mail, AOP
- **Mustache** templates, **Bootstrap** and **Font Awesome** UI
- **MySQL** database
- **Gradle** build

### Running it
1. Create a MySQL database.
2. Create `src/main/resources/application.properties` with your connection settings (see the example in the Spanish section).
3. Run `./gradlew bootRun` and open `http://localhost:8080`.

---

👤 **Sebastián Ariel Vera** · [LinkedIn](https://www.linkedin.com/in/sebastian-ariel-vera-9b240735)
