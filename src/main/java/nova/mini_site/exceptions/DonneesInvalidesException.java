package nova.mini_site.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée quand la requête est bien formée mais contient
 * des données invalides (OTP incorrect, OTP expiré, compte déjà activé, etc.).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DonneesInvalidesException extends RuntimeException {

    public DonneesInvalidesException(String message) {
        super(message);
    }
}
