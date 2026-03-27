# Red Social - Gestión de Amigos

Este proyecto es una aplicación web en Java con Spring Boot que simula una red social básica para gestionar amigos. Utiliza listas en memoria (ArrayList) sin base de datos.

## Tecnologías Utilizadas
- Java 17
- Spring Boot 3.2.0
- Thymeleaf
- HTML y CSS
- Maven

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com/example/redsocial/
│   │       ├── RedSocialApplication.java
│   │       ├── RedSocialController.java
│   │       └── Usuario.java
│   └── resources/
│       ├── static/
│       │   └── css/
│       │       └── styles.css
│       └── templates/
│           ├── index.html
│           └── usuarios.html
└── test/
    └── java/
        └── com/example/redsocial/
            └── RedSocialApplicationTests.java
```

## Funcionalidades
- Crear usuarios con listas de amigos.
- Agregar y eliminar amigos (sin duplicados, no a sí mismo).
- Ver amigos en común entre dos usuarios.
- Sugerir amigos basados en amigos de amigos.
- Interfaz web simple con formularios.

## Instrucciones para Ejecutar
1. Asegúrate de tener Java 17 y Maven instalados.
2. Clona o descarga el proyecto.
3. Navega al directorio del proyecto: `cd red-social`
4. Ejecuta el proyecto: `mvn spring-boot:run`
5. Abre tu navegador y ve a `http://localhost:8080`

## Usuarios de Ejemplo
- Alice (amigos: Bob, Charlie)
- Bob (amigos: Alice, Diana)
- Charlie (amigos: Alice, Eve)
- Diana (amigos: Bob)
- Eve (amigos: Charlie)

## Notas
- Los datos se almacenan en memoria y se pierden al reiniciar la aplicación.
- Validaciones básicas implementadas en el backend.