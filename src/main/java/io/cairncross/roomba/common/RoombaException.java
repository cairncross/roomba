package io.cairncross.roomba.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class RoombaException extends Exception {

    private HttpStatus httpStatus;
    private String message;

    public RoombaException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
