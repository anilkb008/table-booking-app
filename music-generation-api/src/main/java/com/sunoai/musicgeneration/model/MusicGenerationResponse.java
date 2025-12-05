package com.sunoai.musicgeneration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for music generation results
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MusicGenerationResponse {

    /**
     * Unique request ID for tracking
     */
    private String requestId;

    /**
     * Current status of the generation
     */
    private GenerationStatus status;

    /**
     * List of generated songs (Suno typically generates 2 variations)
     */
    private List<GeneratedSong> songs;

    /**
     * Error message if generation failed
     */
    private String errorMessage;

    /**
     * Time when generation was requested
     */
    private Instant createdAt;

    /**
     * Time when generation was completed
     */
    private Instant completedAt;

    /**
     * Credits used for this generation
     */
    private Integer creditsUsed;

    /**
     * Remaining credits in the account
     */
    private Integer creditsRemaining;

    /**
     * Represents a single generated song
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GeneratedSong {
        /**
         * Unique song ID from Suno
         */
        private String songId;

        /**
         * Title of the song
         */
        private String title;

        /**
         * URL to stream/download the audio
         */
        private String audioUrl;

        /**
         * URL to the song's cover image
         */
        private String imageUrl;

        /**
         * URL to the video version (if available)
         */
        private String videoUrl;

        /**
         * Generated or provided lyrics
         */
        private String lyrics;

        /**
         * Music style/genre tags
         */
        private String style;

        /**
         * Duration in seconds
         */
        private Double duration;

        /**
         * Song generation status
         */
        private SongStatus songStatus;

        /**
         * Model used for generation
         */
        private String model;

        /**
         * Creation timestamp
         */
        private Instant createdAt;
    }

    /**
     * Status of a single song
     */
    public enum SongStatus {
        QUEUED,
        PROCESSING,
        STREAMING,
        COMPLETE,
        ERROR
    }

    /**
     * Overall generation status
     */
    public enum GenerationStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        PARTIAL,
        FAILED
    }
}
