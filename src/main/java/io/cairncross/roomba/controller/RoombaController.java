package io.cairncross.roomba.controller;

import io.cairncross.roomba.common.RoombaException;
import io.cairncross.roomba.model.Input;
import io.cairncross.roomba.model.Output;
import io.cairncross.roomba.service.RoombaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoombaController {

    @Autowired
    private RoombaService roombaService;

//    @GetMapping("/input")
//    public ResponseEntity<String> input() {
//        return roombaService.processInput();
//    }

    @PostMapping("/input")
    public ResponseEntity<Output> input(@RequestBody Input input) throws RoombaException {
        Output output = roombaService.processInput(input);
        return ResponseEntity.ok(output);
    }
}
