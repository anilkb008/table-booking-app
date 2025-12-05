# Suno.ai Music Generation API

A Java Spring Boot API for automating music generation using Suno.ai.

## Features

- **Generate Music from Text Prompts**: Describe the music you want and let AI create it
- **Custom Lyrics Mode**: Provide your own lyrics for music generation
- **Song Extension**: Continue/extend existing songs from a specific timestamp
- **Lyrics Generation**: Generate song lyrics using AI
- **Status Polling**: Check the status of generated songs
- **Credits Management**: Monitor your Suno.ai credit balance

## Prerequisites

- Java 17 or higher
- Maven 3.8 or higher
- Suno.ai API key

## Quick Start

### 1. Clone and Build

```bash
cd music-generation-api
mvn clean install
```

### 2. Configure API Key

Set your Suno.ai API key as an environment variable:

```bash
export SUNO_API_KEY=your-api-key-here
```

Or update `src/main/resources/application.yml`:

```yaml
suno:
  api:
    api-key: your-api-key-here
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## API Endpoints

### Generate Music

**POST** `/api/v1/music/generate`

Generate music from a text prompt:

```json
{
  "prompt": "A happy pop song about summer adventures",
  "instrumental": false,
  "waitForCompletion": false
}
```

### Generate Music with Custom Lyrics

**POST** `/api/v1/music/generate/custom`

Generate music with your own lyrics:

```json
{
  "lyrics": "[Verse]\nWalking down the sunny street...",
  "style": "pop, upbeat, acoustic",
  "title": "Summer Days",
  "instrumental": false
}
```

### Get Song Status

**GET** `/api/v1/music/status?ids=song-id-1,song-id-2`

Check the status of generated songs.

**GET** `/api/v1/music/status/{songId}`

Get status of a single song.

### Extend Song

**POST** `/api/v1/music/extend`

Continue an existing song:

```json
{
  "songId": "existing-song-id",
  "continueAt": 30.0,
  "prompt": "Add an epic guitar solo"
}
```

### Generate Lyrics

**POST** `/api/v1/music/lyrics/generate`

Generate lyrics using AI:

```json
{
  "prompt": "A love song about long distance relationships"
}
```

### Get Credits Info

**GET** `/api/v1/music/credits`

Get your current credit balance.

### Health Check

**GET** `/api/v1/music/health`

Check if the API is running.

## Response Example

```json
{
  "requestId": "550e8400-e29b-41d4-a716-446655440000",
  "status": "COMPLETED",
  "songs": [
    {
      "songId": "abc123",
      "title": "Summer Adventures",
      "audioUrl": "https://cdn.suno.ai/abc123.mp3",
      "imageUrl": "https://cdn.suno.ai/abc123.png",
      "lyrics": "[Verse 1]\nSunshine on my face...",
      "style": "pop, upbeat",
      "duration": 120.5,
      "songStatus": "COMPLETE"
    }
  ],
  "creditsRemaining": 450,
  "createdAt": "2024-01-15T10:30:00Z",
  "completedAt": "2024-01-15T10:32:00Z"
}
```

## Configuration Options

| Property | Description | Default |
|----------|-------------|---------|
| `suno.api.base-url` | Suno.ai API base URL | `https://studio-api.suno.ai` |
| `suno.api.api-key` | Your Suno.ai API key | Required |
| `suno.api.timeout` | Request timeout (seconds) | 60 |
| `suno.api.max-retries` | Max retry attempts | 3 |
| `suno.api.polling-interval-seconds` | Polling interval | 5 |
| `suno.api.polling-max-attempts` | Max polling attempts | 60 |
| `suno.api.polling-timeout-minutes` | Polling timeout | 10 |

## API Documentation

Once running, access the Swagger UI at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Project Structure

```
music-generation-api/
├── src/main/java/com/sunoai/musicgeneration/
│   ├── MusicGenerationApplication.java    # Main application
│   ├── controller/
│   │   └── MusicGenerationController.java # REST endpoints
│   ├── service/
│   │   ├── MusicGenerationService.java    # Business logic
│   │   └── SunoApiClient.java             # HTTP client
│   ├── model/
│   │   ├── MusicGenerationRequest.java    # Request DTOs
│   │   ├── MusicGenerationResponse.java   # Response DTOs
│   │   └── ...
│   ├── config/
│   │   ├── SunoApiConfig.java             # API configuration
│   │   ├── WebClientConfig.java           # HTTP client config
│   │   └── OpenApiConfig.java             # Swagger config
│   └── exception/
│       ├── GlobalExceptionHandler.java    # Error handling
│       └── ...
└── src/main/resources/
    └── application.yml                     # Application config
```

## Error Handling

The API returns consistent error responses:

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid request parameters",
  "validationErrors": {
    "prompt": "Prompt is required"
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## License

MIT License
