package com.sunoai.musicgeneration.service;

import com.sunoai.musicgeneration.config.SunoApiConfig;
import com.sunoai.musicgeneration.exception.SunoApiException;
import com.sunoai.musicgeneration.model.SunoApiRequest;
import com.sunoai.musicgeneration.model.SunoApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * HTTP client for communicating with Suno.ai API
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SunoApiClient {

    private final WebClient webClient;
    private final SunoApiConfig config;

    /**
     * Generate music using description mode (AI generates lyrics based on prompt)
     */
    public Mono<SunoApiResponse> generateMusic(SunoApiRequest request) {
        log.info("Sending music generation request to Suno API");

        return webClient.post()
                .uri(config.getBaseUrl() + "/api/generate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new SunoApiException(
                            "Suno API error: " + response.statusCode() + " - " + body))))
                .bodyToMono(SunoApiResponse.class)
                .retryWhen(Retry.backoff(config.getMaxRetries(), Duration.ofSeconds(2))
                        .filter(this::isRetryableException)
                        .doBeforeRetry(signal ->
                            log.warn("Retrying Suno API call, attempt: {}", signal.totalRetries() + 1)))
                .doOnSuccess(response -> log.info("Music generation request successful"))
                .doOnError(error -> log.error("Music generation request failed", error));
    }

    /**
     * Generate music using custom mode (user provides lyrics)
     */
    public Mono<SunoApiResponse> generateMusicCustom(SunoApiRequest request) {
        log.info("Sending custom music generation request to Suno API");

        return webClient.post()
                .uri(config.getBaseUrl() + "/api/custom_generate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new SunoApiException(
                            "Suno API error: " + response.statusCode() + " - " + body))))
                .bodyToMono(SunoApiResponse.class)
                .retryWhen(Retry.backoff(config.getMaxRetries(), Duration.ofSeconds(2))
                        .filter(this::isRetryableException))
                .doOnSuccess(response -> log.info("Custom music generation request successful"))
                .doOnError(error -> log.error("Custom music generation request failed", error));
    }

    /**
     * Get status of songs by their IDs
     */
    public Mono<SunoApiResponse> getSongStatus(List<String> songIds) {
        log.info("Fetching status for {} songs", songIds.size());

        String idsParam = String.join(",", songIds);

        return webClient.get()
                .uri(config.getBaseUrl() + "/api/get?ids=" + idsParam)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new SunoApiException(
                            "Suno API error: " + response.statusCode() + " - " + body))))
                .bodyToMono(SunoApiResponse.class)
                .retryWhen(Retry.backoff(config.getMaxRetries(), Duration.ofSeconds(2))
                        .filter(this::isRetryableException))
                .doOnSuccess(response -> log.info("Song status fetch successful"))
                .doOnError(error -> log.error("Song status fetch failed", error));
    }

    /**
     * Extend an existing song
     */
    public Mono<SunoApiResponse> extendSong(SunoApiRequest request) {
        log.info("Sending song extension request to Suno API");

        return webClient.post()
                .uri(config.getBaseUrl() + "/api/generate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new SunoApiException(
                            "Suno API error: " + response.statusCode() + " - " + body))))
                .bodyToMono(SunoApiResponse.class)
                .retryWhen(Retry.backoff(config.getMaxRetries(), Duration.ofSeconds(2))
                        .filter(this::isRetryableException))
                .doOnSuccess(response -> log.info("Song extension request successful"))
                .doOnError(error -> log.error("Song extension request failed", error));
    }

    /**
     * Generate lyrics using Suno's AI
     */
    public Mono<Map<String, Object>> generateLyrics(String prompt) {
        log.info("Sending lyrics generation request to Suno API");

        return webClient.post()
                .uri(config.getBaseUrl() + "/api/generate_lyrics")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(Map.of("prompt", prompt))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new SunoApiException(
                            "Suno API error: " + response.statusCode() + " - " + body))))
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .retryWhen(Retry.backoff(config.getMaxRetries(), Duration.ofSeconds(2))
                        .filter(this::isRetryableException))
                .doOnSuccess(response -> log.info("Lyrics generation request successful"))
                .doOnError(error -> log.error("Lyrics generation request failed", error));
    }

    /**
     * Get remaining credits for the account
     */
    public Mono<Map<String, Object>> getCreditsInfo() {
        log.info("Fetching credits information");

        return webClient.get()
                .uri(config.getBaseUrl() + "/api/get_limit")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new SunoApiException(
                            "Suno API error: " + response.statusCode() + " - " + body))))
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .retryWhen(Retry.backoff(config.getMaxRetries(), Duration.ofSeconds(2))
                        .filter(this::isRetryableException))
                .doOnSuccess(response -> log.info("Credits info fetch successful"))
                .doOnError(error -> log.error("Credits info fetch failed", error));
    }

    /**
     * Determine if an exception is retryable
     */
    private boolean isRetryableException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException ex) {
            // Retry on 5xx errors and 429 (rate limiting)
            return ex.getStatusCode().is5xxServerError() ||
                   ex.getStatusCode().value() == 429;
        }
        // Retry on connection errors
        return throwable instanceof java.net.ConnectException ||
               throwable instanceof java.net.SocketTimeoutException;
    }
}
