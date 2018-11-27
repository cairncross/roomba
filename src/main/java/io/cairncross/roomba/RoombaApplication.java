package io.cairncross.roomba;

import io.cairncross.roomba.service.RoombaService;
import io.cairncross.roomba.service.RoombaServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RoombaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoombaApplication.class, args);
	}

	@Bean
	public RoombaService roombaService(){
		return new RoombaServiceImpl();
	}
}
