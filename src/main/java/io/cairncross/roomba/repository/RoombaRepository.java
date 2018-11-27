package io.cairncross.roomba.repository;

import io.cairncross.roomba.model.Input;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoombaRepository extends MongoRepository<Input, String> {

}
