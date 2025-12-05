package com.sunoai.musicgeneration.controller;

import com.sunoai.musicgeneration.model.*;
import com.sunoai.musicgeneration.service.MusicGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Suno.ai music generation API
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/music")
@RequiredArgsConstructor
@Tag(name = "Music Generation", description = "API endpoints for AI music generation using Suno.ai")
public class MusicGenerationController {

    private final MusicGenerationService musicGenerationService;

    /**
     * Generate music from a text prompt
     */
    @PostMapping("/generate")
    @Operation(
        summary = "Generate music",
        description = "Generate music using Suno.ai based on a text prompt. " +
                      "Supports both description mode (AI generates lyrics) and custom mode (user provides lyrics)."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Music generation initiated successfully",
            content = @Content(schema = @Schema(implementation = MusicGenerationResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<MusicGenerationResponse>> generateMusic(
            @Valid @RequestBody MusicGenerationRequest request) {

        log.info("Received music generation request");

        return musicGenerationService.generateMusic(request)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    log.error("Error generating music", e);
                    MusicGenerationResponse errorResponse = MusicGenerationResponse.builder()
                            .status(MusicGenerationResponse.GenerationStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(errorResponse));
                });
    }

    /**
     * Generate music with custom lyrics
     */
    @PostMapping("/generate/custom")
    @Operation(
        summary = "Generate music with custom lyrics",
        description = "Generate music using Suno.ai with user-provided lyrics. " +
                      "You must provide lyrics and optionally style/genre tags."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Music generation initiated successfully",
            content = @Content(schema = @Schema(implementation = MusicGenerationResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<MusicGenerationResponse>> generateMusicCustom(
            @Valid @RequestBody MusicGenerationRequest request) {

        log.info("Received custom music generation request");

        // Force custom mode
        request.setCustomMode(true);

        return musicGenerationService.generateMusic(request)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    log.error("Error generating custom music", e);
                    MusicGenerationResponse errorResponse = MusicGenerationResponse.builder()
                            .status(MusicGenerationResponse.GenerationStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(errorResponse));
                });
    }

    /**
     * Get status of generated songs
     */
    @GetMapping("/status")
    @Operation(
        summary = "Get song status",
        description = "Get the current status and details of one or more songs by their IDs"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Song status retrieved successfully",
            content = @Content(schema = @Schema(implementation = MusicGenerationResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid song IDs"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<MusicGenerationResponse>> getSongStatus(
            @Parameter(description = "Comma-separated list of song IDs")
            @RequestParam("ids") List<String> songIds) {

        log.info("Received status request for {} songs", songIds.size());

        return musicGenerationService.getSongStatus(songIds)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    log.error("Error getting song status", e);
                    MusicGenerationResponse errorResponse = MusicGenerationResponse.builder()
                            .status(MusicGenerationResponse.GenerationStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(errorResponse));
                });
    }

    /**
     * Get status of a single song by ID
     */
    @GetMapping("/status/{songId}")
    @Operation(
        summary = "Get single song status",
        description = "Get the current status and details of a single song by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Song status retrieved successfully",
            content = @Content(schema = @Schema(implementation = MusicGenerationResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Song not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<MusicGenerationResponse>> getSingleSongStatus(
            @Parameter(description = "Song ID")
            @PathVariable String songId) {

        log.info("Received status request for song: {}", songId);

        return musicGenerationService.getSongStatus(List.of(songId))
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    log.error("Error getting song status", e);
                    MusicGenerationResponse errorResponse = MusicGenerationResponse.builder()
                            .status(MusicGenerationResponse.GenerationStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(errorResponse));
                });
    }

    /**
     * Extend an existing song
     */
    @PostMapping("/extend")
    @Operation(
        summary = "Extend a song",
        description = "Continue/extend an existing song from a specific timestamp"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Song extension initiated successfully",
            content = @Content(schema = @Schema(implementation = MusicGenerationResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<MusicGenerationResponse>> extendSong(
            @Valid @RequestBody ExtendSongRequest request) {

        log.info("Received song extension request for song: {}", request.getSongId());

        return musicGenerationService.extendSong(request)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    log.error("Error extending song", e);
                    MusicGenerationResponse errorResponse = MusicGenerationResponse.builder()
                            .status(MusicGenerationResponse.GenerationStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(errorResponse));
                });
    }

    /**
     * Generate lyrics using AI
     */
    @PostMapping("/lyrics/generate")
    @Operation(
        summary = "Generate lyrics",
        description = "Generate song lyrics using Suno.ai's AI based on a text prompt"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lyrics generated successfully",
            content = @Content(schema = @Schema(implementation = LyricsGenerationResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<LyricsGenerationResponse>> generateLyrics(
            @Valid @RequestBody LyricsGenerationRequest request) {

        log.info("Received lyrics generation request");

        return musicGenerationService.generateLyrics(request)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    log.error("Error generating lyrics", e);
                    LyricsGenerationResponse errorResponse = LyricsGenerationResponse.builder()
                            .status("error")
                            .errorMessage(e.getMessage())
                            .build();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(errorResponse));
                });
    }

    /**
     * Get account credits information
     */
    @GetMapping("/credits")
    @Operation(
        summary = "Get credits info",
        description = "Get the current credit balance and usage information for the Suno.ai account"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Credits info retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Map<String, Object>>> getCreditsInfo() {

        log.info("Received credits info request");

        return musicGenerationService.getCreditsInfo()
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    log.error("Error getting credits info", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", e.getMessage())));
                });
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the API is running")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Suno.ai Music Generation API"
        ));
    }
}
