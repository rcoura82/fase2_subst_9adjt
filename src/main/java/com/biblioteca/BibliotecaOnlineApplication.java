package com.biblioteca;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Classe principal da aplicação Biblioteca Online
 * 
 * Sistema de Biblioteca Online desenvolvido com:
 * - Spring Boot 3.2.0
 * - Java 21
 * - Banco de dados JPA
 * - API REST com Swagger/OpenAPI
 * - Processamento com Streams
 * 
 * @author Biblioteca Online Team
 * @version 1.0.0
 */
@SpringBootApplication
public class BibliotecaOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaOnlineApplication.class, args);
    }

    /**
     * Configuração do OpenAPI para documentação Swagger
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Biblioteca Online")
                        .version("1.0.0")
                        .description("Sistema de Biblioteca Online com APIs RESTful para gerenciar livros, usuários e empréstimos. " +
                                "Implementa relatórios avançados com Streams e técnicas modernas de Java.")
                        .contact(new Contact()
                                .name("Biblioteca Online")
                                .url("https://github.com/rcoura82/fase2_subst_9adjt")
                                .email("suporte@biblioteca.online"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
