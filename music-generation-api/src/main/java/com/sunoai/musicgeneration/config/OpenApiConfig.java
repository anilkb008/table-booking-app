package com.sunoai.musicgeneration.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger documentation configuration
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI musicGenerationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Suno.ai Music Generation API")
                        .description("Java API for automating music generation using Suno.ai. " +
                                "This API provides endpoints for generating music from text prompts, " +
                                "custom lyrics, extending existing songs, and generating lyrics using AI.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server"),
                        new Server()
                                .url("https://api.example.com")
                                .description("Production server")
                ));
    }
}
