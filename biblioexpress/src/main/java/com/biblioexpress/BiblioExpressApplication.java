package com.biblioexpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion.
 *
 * La anotacion @SpringBootApplication hace 3 cosas:
 *  - marca esta clase como configuracion de Spring,
 *  - activa la "autoconfiguracion" (Spring arma solo el servidor web, el JSON, etc.),
 *  - escanea este paquete y los de abajo buscando componentes (@RestController, @Service, @Repository...).
 *
 * Al ejecutar main(), Spring Boot levanta un servidor Tomcat embebido en el puerto 8080.
 */
@SpringBootApplication
public class BiblioExpressApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiblioExpressApplication.class, args);
    }
}
