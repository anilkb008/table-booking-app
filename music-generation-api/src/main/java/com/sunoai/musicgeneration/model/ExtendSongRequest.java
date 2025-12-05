package com.sunoai.musicgeneration.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for extending an existing song
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtendSongRequest {

    /**
     * ID of the song to extend from
     */
    @NotBlank(message = "Song ID is required")
    private String songId;

    /**
     * Timestamp (in seconds) to continue from
     */
    @Positive(message = "Continue timestamp must be positive")
    private Double continueAt;

    /**
     * Additional prompt for the extension
     */
    private String prompt;

    /**
     * Additional lyrics for the extension
     */
    private String lyrics;

    /**
     * Style tags for the extension
     */
    private String style;

    /**
     * Whether the extension should be instrumental
     */
    @Builder.Default
    private boolean instrumental = false;
}
