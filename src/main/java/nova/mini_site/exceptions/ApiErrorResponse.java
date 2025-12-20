package nova.mini_site.exceptions;

import java.time.Instant;

/**
 * Format simple et stable pour les erreurs API.
 */
public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
