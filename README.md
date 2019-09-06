# Ip Tracker

Aplicación para obtener información de un pais a través de una dirección IP


### Prerrequisitos

El proyecto requiere JDK 11 y Apache Maven
* [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) - JDK 11
* [Maven](https://maven.apache.org/) - Dependency Management


### Despliegue con Docker

Compilar el proyecto con Maven

```
mvn clean package
```

Correr el docker-compose

```
docker-compose up --build
```

Ingresar a la aplicación 

http://localhost:8080

Para ver las estadísticas

http://localhost:8080/stats
