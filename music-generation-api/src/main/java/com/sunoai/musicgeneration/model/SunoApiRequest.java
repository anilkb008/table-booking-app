package com.sunoai.musicgeneration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Internal request model for Suno.ai API calls
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SunoApiRequest {

    /**
     * Text prompt for music generation
     */
    @JsonProperty("prompt")
    private String prompt;

    /**
     * Custom lyrics (for custom mode)
     */
    @JsonProperty("lyrics")
    private String lyrics;

    /**
     * Music style/genre tags
     */
    @JsonProperty("tags")
    private String tags;

    /**
     * Song title
     */
    @JsonProperty("title")
    private String title;

    /**
     * Whether to make instrumental
     */
    @JsonProperty("make_instrumental")
    private Boolean makeInstrumental;

    /**
     * Model version to use
     */
    @JsonProperty("mv")
    private String modelVersion;

    /**
     * Generation mode: "description" or "custom"
     */
    @JsonProperty("gpt_description_prompt")
    private String gptDescriptionPrompt;

    /**
     * Continue from a specific clip
     */
    @JsonProperty("continue_clip_id")
    private String continueClipId;

    /**
     * Continue at specific timestamp
     */
    @JsonProperty("continue_at")
    private Double continueAt;

    /**
     * Factory method for description-based generation
     */
    public static SunoApiRequest fromDescriptionMode(MusicGenerationRequest request) {
        return SunoApiRequest.builder()
                .gptDescriptionPrompt(request.getPrompt())
                .makeInstrumental(request.isInstrumental())
                .modelVersion(request.getModel())
                .build();
    }

    /**
     * Factory method for custom lyrics mode
     */
    public static SunoApiRequest fromCustomMode(MusicGenerationRequest request) {
        return SunoApiRequest.builder()
                .prompt(request.getLyrics())
                .tags(request.getStyle())
                .title(request.getTitle())
                .makeInstrumental(request.isInstrumental())
                .modelVersion(request.getModel())
                .build();
    }
}
