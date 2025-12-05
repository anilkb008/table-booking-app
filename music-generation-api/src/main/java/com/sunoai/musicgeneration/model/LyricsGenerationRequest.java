package com.sunoai.musicgeneration.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for generating lyrics using Suno.ai
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LyricsGenerationRequest {

    /**
     * Prompt describing the lyrics to generate
     */
    @NotBlank(message = "Prompt is required")
    @Size(max = 1000, message = "Prompt must not exceed 1000 characters")
    private String prompt;
}
