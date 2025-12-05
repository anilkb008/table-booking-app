package com.sunoai.musicgeneration.exception;

/**
 * Exception thrown when music generation fails
 */
public class MusicGenerationException extends RuntimeException {

    public MusicGenerationException(String message) {
        super(message);
    }

    public MusicGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
