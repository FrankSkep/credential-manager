# Credential Manager

## Descripción

Aplicación de escritorio desarrollada en Java con Swing, diseñada para gestionar contraseñas de forma segura. Permite a los usuarios almacenar, organizar y acceder a sus contraseñas utilizando una base de datos local en formato `.db` con ubicación dinámica.

![Imagen](https://raw.githubusercontent.com/FrankSkep/Credential-Manager/refs/heads/main/screens/2.png?token=GHSAT0AAAAAACU2EQKLMVXQKS3CI6MWK7HYZXTT5UA)

## Características

- **Seguridad**: Registro y autenticación de usuarios para proteger el acceso a la aplicación.
- **Gestión Segura de Contraseñas**: Almacenamiento y organización de contraseñas de forma segura, utilizando encriptación con clave secreta de 16 bits.
- **Cambio y Creación de Base de Datos**: Opción para cambiar entre diferentes bases de datos o crear nuevas, en cualquier ubicacion que el usuario desee. (Se requiere autenticación con las credenciales del dueño de la base de datos a la que desea cambiar).
- **Búsqueda Rápida**: Funcionalidad de búsqueda en tiempo real para encontrar contraseñas con nombre del servicio, nombre de usuario o categoria.
- **Filtrado por Categorías y Servicios**: Posibilidad de clasificar contraseñas en categorías y servicios para un acceso más ordenado y fácil.
- **Interfaz**: Interfaz gráfica de usuario (GUI) diseñada con Java Swing.
- **Patrón Singleton**: Implementación del patrón de diseño Singleton en la gestion de la conexión a la base de datos, el `PasswordDAO`, y el panel principal de la aplicación (`DashboardPNL`).
- **HikariCP**: Integración de HikariCP para la gestión eficiente de conexiones a la base de datos, mejorando el rendimiento y la estabilidad.

## Requisitos

- **Java Development Kit (JDK)**: 17 o superior.
- **Dependencias Externas**:
  - SQLite JDBC
  - HikariCP
  - Lombok

## Uso

1. Al iniciar la aplicación, selecciona o crea una base de datos.
2. Regístrate/Inicia sesion proporcionando un nombre de usuario y una contraseña.
3. Agrega nuevas contraseñas.
4. Utiliza la función de búsqueda para localizar contraseñas rápidamente.
5. Cambia entre bases de datos y gestiona múltiples cuentas de usuario, proporcionando las credenciales correspondientes.
6. Las contraseñas se encriptan y desencriptan utilizando una clave secreta, garantizando la seguridad de la información.
