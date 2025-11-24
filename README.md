# Sistema Empresarial (Spring Boot 3, Java 17, PostgreSQL)

## Requisitos
- Java 17+
- Maven
- **PostgreSQL 14+** (Migrado desde MySQL)

## Configuración
1. Cree la base de datos ejecutando el script proporcionado en PostgreSQL. Como mínimo:
   ```sql
   CREATE DATABASE sistema_empresarial;
   ```
2. Edite `src/main/resources/application.properties` con su usuario/clave de **PostgreSQL**.
3. Construya y ejecute:
   ```bash
   mvn spring-boot:run
   ```
4. Si no hay usuarios, abra `http://localhost:8080/setup/admin` y registre el **Administrador inicial** (contraseña con al menos 8 caracteres y un número).
5. Inicie sesión en `http://localhost:8080/login`.

## Módulos incluidos
- Autenticación y autorización (roles y permisos cargados desde BD).
- Dashboard por rol (vistas base).
- Usuarios (CRUD simple de ejemplo).
- Inventario (Productos + ajuste de stock con registro de movimiento).
- Estructura preparada para Ventas y Compras.

> Nota: Para asignar automáticamente todos los permisos al rol ADMIN en el setup, asegúrese de haber insertado los registros de `permisos` y `roles` en la BD según su script.
