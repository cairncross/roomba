package io.cairncross.roomba.repository;

import io.cairncross.roomba.model.Output;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OutputRepository extends MongoRepository<Output, String> {

}
