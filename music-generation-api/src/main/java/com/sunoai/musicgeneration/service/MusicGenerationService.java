package com.sunoai.musicgeneration.service;

import com.sunoai.musicgeneration.config.SunoApiConfig;
import com.sunoai.musicgeneration.exception.MusicGenerationException;
import com.sunoai.musicgeneration.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Main service for music generation operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MusicGenerationService {

    private final SunoApiClient sunoApiClient;
    private final SunoApiConfig config;

    /**
     * Generate music based on the provided request
     */
    public Mono<MusicGenerationResponse> generateMusic(MusicGenerationRequest request) {
        log.info("Starting music generation with prompt: {}", truncateForLog(request.getPrompt()));

        String requestId = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // Determine which API to use based on request
        Mono<SunoApiResponse> apiCall;
        if (request.isCustomMode() && request.getLyrics() != null) {
            apiCall = sunoApiClient.generateMusicCustom(SunoApiRequest.fromCustomMode(request));
        } else {
            apiCall = sunoApiClient.generateMusic(SunoApiRequest.fromDescriptionMode(request));
        }

        return apiCall
                .flatMap(sunoResponse -> {
                    if (sunoResponse.getError() != null) {
                        return Mono.error(new MusicGenerationException(sunoResponse.getError()));
                    }

                    // Build initial response
                    MusicGenerationResponse response = buildResponse(requestId, sunoResponse, createdAt);

                    // If wait for completion is enabled, poll until done
                    if (request.isWaitForCompletion()) {
                        List<String> songIds = response.getSongs().stream()
                                .map(MusicGenerationResponse.GeneratedSong::getSongId)
                                .collect(Collectors.toList());

                        return pollForCompletion(songIds, requestId, createdAt);
                    }

                    return Mono.just(response);
                })
                .onErrorMap(e -> {
                    if (e instanceof MusicGenerationException) {
                        return e;
                    }
                    return new MusicGenerationException("Failed to generate music: " + e.getMessage(), e);
                });
    }

    /**
     * Get status of songs by their IDs
     */
    public Mono<MusicGenerationResponse> getSongStatus(List<String> songIds) {
        log.info("Fetching status for {} songs", songIds.size());

        String requestId = UUID.randomUUID().toString();

        return sunoApiClient.getSongStatus(songIds)
                .map(sunoResponse -> buildResponse(requestId, sunoResponse, Instant.now()))
                .onErrorMap(e -> new MusicGenerationException("Failed to get song status: " + e.getMessage(), e));
    }

    /**
     * Extend an existing song
     */
    public Mono<MusicGenerationResponse> extendSong(ExtendSongRequest request) {
        log.info("Extending song: {}", request.getSongId());

        String requestId = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        SunoApiRequest sunoRequest = SunoApiRequest.builder()
                .continueClipId(request.getSongId())
                .continueAt(request.getContinueAt())
                .gptDescriptionPrompt(request.getPrompt())
                .prompt(request.getLyrics())
                .tags(request.getStyle())
                .makeInstrumental(request.isInstrumental())
                .build();

        return sunoApiClient.extendSong(sunoRequest)
                .map(sunoResponse -> buildResponse(requestId, sunoResponse, createdAt))
                .onErrorMap(e -> new MusicGenerationException("Failed to extend song: " + e.getMessage(), e));
    }

    /**
     * Generate lyrics using AI
     */
    public Mono<LyricsGenerationResponse> generateLyrics(LyricsGenerationRequest request) {
        log.info("Generating lyrics with prompt: {}", truncateForLog(request.getPrompt()));

        return sunoApiClient.generateLyrics(request.getPrompt())
                .map(this::buildLyricsResponse)
                .onErrorMap(e -> new MusicGenerationException("Failed to generate lyrics: " + e.getMessage(), e));
    }

    /**
     * Get account credits information
     */
    public Mono<Map<String, Object>> getCreditsInfo() {
        log.info("Fetching credits information");

        return sunoApiClient.getCreditsInfo()
                .onErrorMap(e -> new MusicGenerationException("Failed to get credits info: " + e.getMessage(), e));
    }

    /**
     * Poll for song completion with exponential backoff
     */
    private Mono<MusicGenerationResponse> pollForCompletion(List<String> songIds, String requestId, Instant createdAt) {
        return Flux.interval(Duration.ofSeconds(5))
                .take(config.getPollingMaxAttempts())
                .flatMap(i -> sunoApiClient.getSongStatus(songIds))
                .takeUntil(response -> isGenerationComplete(response))
                .last()
                .map(response -> {
                    MusicGenerationResponse result = buildResponse(requestId, response, createdAt);
                    result.setCompletedAt(Instant.now());
                    return result;
                })
                .timeout(Duration.ofMinutes(config.getPollingTimeoutMinutes()))
                .onErrorMap(e -> new MusicGenerationException("Polling for completion timed out: " + e.getMessage(), e));
    }

    /**
     * Check if all songs are complete
     */
    private boolean isGenerationComplete(SunoApiResponse response) {
        if (response.getClips() == null || response.getClips().isEmpty()) {
            return false;
        }

        return response.getClips().stream()
                .allMatch(clip -> "complete".equalsIgnoreCase(clip.getStatus()) ||
                                  "error".equalsIgnoreCase(clip.getStatus()));
    }

    /**
     * Build response from Suno API response
     */
    private MusicGenerationResponse buildResponse(String requestId, SunoApiResponse sunoResponse, Instant createdAt) {
        List<MusicGenerationResponse.GeneratedSong> songs = null;

        if (sunoResponse.getClips() != null) {
            songs = sunoResponse.getClips().stream()
                    .map(this::convertClipToSong)
                    .collect(Collectors.toList());
        }

        // Determine overall status
        MusicGenerationResponse.GenerationStatus status = determineOverallStatus(songs);

        return MusicGenerationResponse.builder()
                .requestId(requestId)
                .status(status)
                .songs(songs)
                .creditsRemaining(sunoResponse.getCreditsLeft())
                .createdAt(createdAt)
                .errorMessage(sunoResponse.getError())
                .build();
    }

    /**
     * Convert Suno clip to GeneratedSong
     */
    private MusicGenerationResponse.GeneratedSong convertClipToSong(SunoApiResponse.SunoClip clip) {
        MusicGenerationResponse.SongStatus songStatus = switch (clip.getStatus() != null ? clip.getStatus().toLowerCase() : "") {
            case "queued" -> MusicGenerationResponse.SongStatus.QUEUED;
            case "processing" -> MusicGenerationResponse.SongStatus.PROCESSING;
            case "streaming" -> MusicGenerationResponse.SongStatus.STREAMING;
            case "complete" -> MusicGenerationResponse.SongStatus.COMPLETE;
            case "error" -> MusicGenerationResponse.SongStatus.ERROR;
            default -> MusicGenerationResponse.SongStatus.PROCESSING;
        };

        String lyrics = null;
        String style = null;
        if (clip.getMetadata() != null) {
            lyrics = clip.getMetadata().getPrompt();
            style = clip.getMetadata().getTags();
        }

        return MusicGenerationResponse.GeneratedSong.builder()
                .songId(clip.getId())
                .title(clip.getTitle())
                .audioUrl(clip.getAudioUrl())
                .imageUrl(clip.getImageUrl())
                .videoUrl(clip.getVideoUrl())
                .lyrics(lyrics)
                .style(style)
                .duration(clip.getDuration())
                .songStatus(songStatus)
                .model(clip.getModelName())
                .createdAt(clip.getCreatedAt() != null ? Instant.parse(clip.getCreatedAt()) : null)
                .build();
    }

    /**
     * Determine overall generation status from songs
     */
    private MusicGenerationResponse.GenerationStatus determineOverallStatus(List<MusicGenerationResponse.GeneratedSong> songs) {
        if (songs == null || songs.isEmpty()) {
            return MusicGenerationResponse.GenerationStatus.PENDING;
        }

        boolean allComplete = songs.stream()
                .allMatch(s -> s.getSongStatus() == MusicGenerationResponse.SongStatus.COMPLETE);

        boolean anyError = songs.stream()
                .anyMatch(s -> s.getSongStatus() == MusicGenerationResponse.SongStatus.ERROR);

        boolean anyComplete = songs.stream()
                .anyMatch(s -> s.getSongStatus() == MusicGenerationResponse.SongStatus.COMPLETE);

        if (allComplete) {
            return MusicGenerationResponse.GenerationStatus.COMPLETED;
        } else if (anyError && anyComplete) {
            return MusicGenerationResponse.GenerationStatus.PARTIAL;
        } else if (anyError) {
            return MusicGenerationResponse.GenerationStatus.FAILED;
        } else {
            return MusicGenerationResponse.GenerationStatus.IN_PROGRESS;
        }
    }

    /**
     * Build lyrics response from API response
     */
    private LyricsGenerationResponse buildLyricsResponse(Map<String, Object> response) {
        return LyricsGenerationResponse.builder()
                .id(String.valueOf(response.get("id")))
                .title(String.valueOf(response.get("title")))
                .lyrics(String.valueOf(response.get("text")))
                .status(String.valueOf(response.getOrDefault("status", "complete")))
                .build();
    }

    /**
     * Truncate string for logging
     */
    private String truncateForLog(String text) {
        if (text == null) return null;
        return text.length() > 100 ? text.substring(0, 100) + "..." : text;
    }
}
