package com.sunoai.musicgeneration.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for checking status of generated songs
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongStatusRequest {

    /**
     * List of song IDs to check status for
     */
    @NotEmpty(message = "At least one song ID is required")
    private List<String> songIds;
}
