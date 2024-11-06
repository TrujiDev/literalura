# Literalura

**Literalura** es una aplicación basada en **Spring Boot** que permite gestionar información de libros y autores. La aplicación se conecta a una API externa para obtener datos sobre libros, como el título, el autor, el idioma y el número de descargas, y luego almacena esta información en una base de datos relacional. Esta aplicación está diseñada para manejar información sobre autores y libros, permitiendo realizar búsquedas y consultar datos almacenados.

## Tecnologías utilizadas

- **Spring Boot**: Framework principal para la construcción de aplicaciones Java basadas en Spring.
- **JPA (Java Persistence API)**: Utilizado para la persistencia de datos en una base de datos relacional.
- **Jackson**: Para la conversión de JSON a objetos Java y viceversa.
- **SLF4J**: Para el manejo de logs en la aplicación.
- **H2 Database**: Base de datos en memoria (por defecto), para almacenar datos de libros y autores.
- **HttpClient**: Para consumir una API externa que proporciona información sobre libros.

## Estructura del proyecto

La estructura del proyecto se divide en varios paquetes:

- `model`: Contiene las clases que representan las entidades que interactúan con la base de datos.
  - `Author`: Representa a los autores de los libros.
  - `Book`: Representa los libros, con su título, autor, idioma y número de descargas.
  - `AuthorData` y `BookData`: Clases utilizadas para mapear los datos recibidos de la API externa.
  
- `contracts`: Contiene interfaces para los componentes de la aplicación, como la interfaz `IJsonParser`, que define un método para convertir JSON a objetos Java.

- `repository`: Contiene los repositorios que interactúan con la base de datos utilizando **JPA**.
  - `BookRepository`: Repositorio para acceder y realizar consultas sobre los libros.
  - `AuthorRepository`: Repositorio para acceder y realizar consultas sobre los autores.

- `service`: Contiene la lógica de negocio de la aplicación.
  - `ApiClient`: Cliente para consumir la API externa y obtener información sobre libros.
  - `JsonParser`: Implementación de la interfaz `IJsonParser` para convertir JSON a objetos Java.

## Funcionalidades

### Consumo de API

La aplicación consume una API externa para obtener información sobre libros. Esta API proporciona datos como el título, el autor, el idioma y el número de descargas. Los datos recibidos son almacenados en la base de datos local para futuras consultas.

### Gestión de Autores y Libros

- **Autores**: Se pueden registrar y consultar autores, junto con su fecha de nacimiento y de fallecimiento.
- **Libros**: Los libros se almacenan junto con su título, autor (relacionado con la tabla de autores), idioma y el número de descargas.

## Instalación

### Requisitos

- **JDK 11 o superior**
- **Maven** para la gestión de dependencias
- **IDE** como IntelliJ IDEA o Eclipse

### Pasos para ejecutar el proyecto

1. Clona este repositorio:
   ```bash
   git clone https://github.com/TrujiDev/literalura.git
   ```

2. Navega al directorio del proyecto:
   ```bash
   cd literalura
   ```

3. Ejecuta el proyecto con Maven:
   ```bash
   mvn spring-boot:run
   ```

4. La aplicación se iniciará en `http://localhost:8080`.

## API

La aplicación no expone una API RESTful en su forma actual, pero realiza el consumo de una API externa para obtener los datos de libros.

## Contribuciones

Si deseas contribuir a este proyecto, siéntete libre de hacer un **fork** y abrir un **pull request** con tus cambios. Asegúrate de seguir las mejores prácticas de codificación y de realizar pruebas antes de enviar tus contribuciones.