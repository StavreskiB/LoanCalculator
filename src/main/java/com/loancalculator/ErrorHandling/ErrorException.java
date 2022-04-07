package com.loancalculator.ErrorHandling;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorException extends RuntimeException {

    private HttpStatus status = null;

    private Object data = null;

    public ErrorException() {
        super();
    }

    public ErrorException(String message) {
        super(message);
    }

    public ErrorException(HttpStatus status, String message) {
        this(message);
        this.status = status;
    }

    public ErrorException(HttpStatus status, String message, Object data) {
        this(status, message);
        this.data = data;
    }
}
