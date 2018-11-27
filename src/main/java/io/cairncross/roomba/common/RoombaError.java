package io.cairncross.roomba.common;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class RoombaError {

    private HttpStatus httpStatus;
    private String message;

    public RoombaError (HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
