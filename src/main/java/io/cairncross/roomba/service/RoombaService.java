package io.cairncross.roomba.service;

import io.cairncross.roomba.common.RoombaException;
import io.cairncross.roomba.model.Input;
import io.cairncross.roomba.model.Output;

public interface RoombaService {

    Output processInput(Input input) throws RoombaException;
}
