package io.cairncross.roomba.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RoombaExceptionHandler {

    @ExceptionHandler(RoombaException.class)
    protected ResponseEntity<RoombaError> handleRoombaException(RoombaException ex) {
        RoombaError error = new RoombaError(ex.getHttpStatus(), ex.getMessage());
        return ResponseEntity.status(error.getHttpStatus()).body(error);
    }
}
