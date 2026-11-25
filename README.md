# Quiz Programado: CRUD con relaciones 1 a muchos (Departamentos y Empleados)

**Estudiante:** Dahiana Solano  
**Curso:** Programación IV – Lenguajes de Cuarta Generación  
**Tecnologías:** Java 17, Spring Boot, Thymeleaf, Spring Security, Spring Data JPA, H2 Database.

## Cómo ejecutar el proyecto
1. Clona este repositorio o descarga el código fuente.
2. Abre una terminal en la raíz del proyecto.
3. Ejecuta el siguiente comando:
   ```bash
   mvn clean spring-boot:run
   ```
4. Abre tu navegador y ve a: `http://localhost:8080`

## Usuarios de prueba
El sistema cuenta con dos usuarios iniciales creados automáticamente:
* **Administrador (ADMIN):** Usuario: `admin` | Contraseña: `admin123`
* **Usuario normal (USER):** Usuario: `user` | Contraseña: `user123`

*(El usuario ADMIN puede crear, editar y eliminar. El usuario USER solo puede ver las listas de departamentos y empleados).*

## Archivos creados y modificados por requerimiento

* **R1 - Repositorios:**
  * `repository/DepartamentoRepository.java`
  * `repository/EmpleadoRepository.java`
* **R2 - Servicio de Departamentos:**
  * `service/DepartamentoService.java`
* **R3 - Servicio de Empleados:**
  * `service/EmpleadoService.java`
* **R4 - Controladores:**
  * `controller/DepartamentoController.java`
  * `controller/EmpleadoController.java`
* **R5 - Vistas (Thymeleaf):**
  * `templates/departamentos/lista.html`, `formulario.html`
  * `templates/empleados/lista.html`, `formulario.html`
  * `templates/index.html`, `templates/login.html`
* **R6 - Seguridad:**
  * `config/SecurityConfig.java`
  * `service/UsuarioDetailsService.java`
  * `model/Usuario.java`, `repository/UsuarioRepository.java`
* **R7 - Datos iniciales:**
  * `config/DataInitializer.java`