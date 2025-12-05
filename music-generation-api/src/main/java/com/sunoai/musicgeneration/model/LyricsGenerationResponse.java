package com.sunoai.musicgeneration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for lyrics generation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LyricsGenerationResponse {

    /**
     * Unique ID for the lyrics generation request
     */
    private String id;

    /**
     * Generated title for the song
     */
    private String title;

    /**
     * Generated lyrics text
     */
    private String lyrics;

    /**
     * Status of the generation
     */
    private String status;

    /**
     * Error message if generation failed
     */
    private String errorMessage;
}
