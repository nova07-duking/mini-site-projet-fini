package nova.mini_site.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NonAutoriseException extends RuntimeException {

    public NonAutoriseException(String message) {
        super(message);
    }
}

