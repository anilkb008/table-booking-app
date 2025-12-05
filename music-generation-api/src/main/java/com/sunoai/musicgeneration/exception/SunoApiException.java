package com.sunoai.musicgeneration.exception;

/**
 * Exception thrown when Suno.ai API returns an error
 */
public class SunoApiException extends RuntimeException {

    public SunoApiException(String message) {
        super(message);
    }

    public SunoApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
