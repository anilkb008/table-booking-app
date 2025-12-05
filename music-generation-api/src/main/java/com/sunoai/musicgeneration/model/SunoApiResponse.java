package com.sunoai.musicgeneration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Internal response model from Suno.ai API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SunoApiResponse {

    /**
     * List of generated clips/songs
     */
    @JsonProperty("clips")
    private List<SunoClip> clips;

    /**
     * Alternative field name for clips
     */
    @JsonProperty("data")
    private List<SunoClip> data;

    /**
     * Credits information
     */
    @JsonProperty("credits_left")
    private Integer creditsLeft;

    /**
     * Error information if request failed
     */
    @JsonProperty("error")
    private String error;

    /**
     * Status of the request
     */
    @JsonProperty("status")
    private String status;

    /**
     * Get clips from either field
     */
    public List<SunoClip> getClips() {
        return clips != null ? clips : data;
    }

    /**
     * Represents a single clip/song from Suno
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SunoClip {

        @JsonProperty("id")
        private String id;

        @JsonProperty("title")
        private String title;

        @JsonProperty("audio_url")
        private String audioUrl;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty("image_large_url")
        private String imageLargeUrl;

        @JsonProperty("video_url")
        private String videoUrl;

        @JsonProperty("model_name")
        private String modelName;

        @JsonProperty("metadata")
        private ClipMetadata metadata;

        @JsonProperty("status")
        private String status;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("duration")
        private Double duration;

        @JsonProperty("is_liked")
        private Boolean isLiked;

        @JsonProperty("is_public")
        private Boolean isPublic;

        @JsonProperty("play_count")
        private Integer playCount;

        @JsonProperty("upvote_count")
        private Integer upvoteCount;

        /**
         * Metadata for a clip
         */
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ClipMetadata {

            @JsonProperty("prompt")
            private String prompt;

            @JsonProperty("gpt_description_prompt")
            private String gptDescriptionPrompt;

            @JsonProperty("tags")
            private String tags;

            @JsonProperty("type")
            private String type;

            @JsonProperty("duration")
            private Double duration;

            @JsonProperty("error_type")
            private String errorType;

            @JsonProperty("error_message")
            private String errorMessage;

            @JsonProperty("concat_history")
            private List<ConcatHistory> concatHistory;
        }

        /**
         * History of concatenated clips
         */
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ConcatHistory {

            @JsonProperty("id")
            private String id;

            @JsonProperty("continue_at")
            private Double continueAt;
        }
    }
}
