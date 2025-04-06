package top.stillmisty.shopback.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserStatusException extends RuntimeException {
    private final HttpStatus status;

    public UserStatusException(String message) {
        this(message, HttpStatus.FORBIDDEN);
    }

    public UserStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}