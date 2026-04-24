# Demostración del Patrón State: Dron de Vuelo

Este proyecto es una aplicación web sencilla en Java orientada a demostrar la implementación del **Patrón de Diseño State** (Estado). El sistema simula las distintas fases de vuelo de un dron (en tierra, chequeos pre-vuelo, armado, volando, retorno a casa y emergencia). A través del backend, el contexto (`FlightContext`) delega las acciones del usuario a interfaces de estado específicas (`FlightState`), las cuales encapsulan las reglas de si una acción es válida o no y gestionan la transición al siguiente estado de forma segura. Esto elimina la necesidad de utilizar estructuras condicionales lógicas complejas y rígidamente acopladas sobre el estado general del dron.

## Cómo ejecutar la aplicación

El proyecto utiliza **Java 21** de forma nativa e incluye un servidor HTTP ligero embebido. Además, gestiona sus dependencias a través de **Maven**. 

Para inicializar y ejecutar la aplicación sigue estos pasos:

1. Abre tu terminal o línea de comandos.
2. Navega hasta la carpeta `demo` del proyecto:
   ```bash
   cd demo
   ```
3. Ejecuta el objetivo de Maven para compilar e iniciar la aplicación mediante el `exec-maven-plugin`:
   ```bash
   mvn clean compile exec:java
   ```
4. Una vez la consola muestre el mensaje de éxito, abre tu navegador web y visita la siguiente dirección para visualizar la interfaz e interactuar con el dron:
   **[http://localhost:8080/](http://localhost:8080/)**
