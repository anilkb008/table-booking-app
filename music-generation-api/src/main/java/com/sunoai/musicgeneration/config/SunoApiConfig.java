package com.sunoai.musicgeneration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Configuration properties for Suno.ai API
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "suno.api")
@Validated
public class SunoApiConfig {

    /**
     * Suno.ai API base URL
     */
    @NotBlank(message = "Suno API base URL is required")
    private String baseUrl = "https://studio-api.suno.ai";

    /**
     * API key for authentication
     */
    @NotBlank(message = "Suno API key is required")
    private String apiKey;

    /**
     * Request timeout in seconds
     */
    @Positive
    private int timeout = 60;

    /**
     * Maximum retry attempts for failed requests
     */
    @Positive
    private int maxRetries = 3;

    /**
     * Polling interval in seconds when waiting for completion
     */
    @Positive
    private int pollingIntervalSeconds = 5;

    /**
     * Maximum polling attempts
     */
    @Positive
    private int pollingMaxAttempts = 60;

    /**
     * Polling timeout in minutes
     */
    @Positive
    private int pollingTimeoutMinutes = 10;
}
