package com.sunoai.musicgeneration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main application class for Suno.ai Music Generation API
 */
@SpringBootApplication
@EnableConfigurationProperties
public class MusicGenerationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicGenerationApplication.class, args);
    }
}
