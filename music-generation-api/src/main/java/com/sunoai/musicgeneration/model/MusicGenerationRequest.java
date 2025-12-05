package com.sunoai.musicgeneration.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for generating music using Suno.ai
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicGenerationRequest {

    /**
     * Text prompt describing the music to generate
     * Example: "A happy pop song about summer adventures"
     */
    @NotBlank(message = "Prompt is required")
    @Size(max = 3000, message = "Prompt must not exceed 3000 characters")
    private String prompt;

    /**
     * Optional: Custom lyrics for the song
     * If provided, Suno will use these lyrics instead of generating them
     */
    @Size(max = 3000, message = "Lyrics must not exceed 3000 characters")
    private String lyrics;

    /**
     * Optional: Music style/genre tags
     * Example: "pop, upbeat, electronic"
     */
    @Size(max = 500, message = "Style must not exceed 500 characters")
    private String style;

    /**
     * Optional: Title for the generated song
     */
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    /**
     * Whether to generate instrumental only (no vocals)
     */
    @Builder.Default
    private boolean instrumental = false;

    /**
     * Whether to use custom mode (provide your own lyrics)
     * If true, lyrics field must be provided
     */
    @Builder.Default
    private boolean customMode = false;

    /**
     * Model version to use (default: chirp-v3-5)
     */
    @Builder.Default
    private String model = "chirp-v3-5";

    /**
     * Duration of the song in seconds (if supported)
     */
    private Integer duration;

    /**
     * Whether to wait for the song to be fully generated
     * If false, returns immediately with song IDs for polling
     */
    @Builder.Default
    private boolean waitForCompletion = false;

    /**
     * Callback URL to notify when generation is complete
     */
    private String callbackUrl;
}
