package io.cairncross.roomba;

import io.cairncross.roomba.common.RoombaException;
import io.cairncross.roomba.model.Input;
import io.cairncross.roomba.model.Output;
import io.cairncross.roomba.service.RoombaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoombaApplicationTests {

	@Autowired
	private RoombaService roombaService;

	@Test
	public void throwsException_givenInvalidRoombaStartingCoords() {
		Input input = Input.builder()
				.roomSize(new int[] {2, 2})
				.coords(new int [] {-1, -1})
				.build();
		assertException(input, HttpStatus.BAD_REQUEST, "Invalid input coords");
	}

	@Test
	public void throwsException_givenInvalidRoomSize() {
		Input input = Input.builder()
				.roomSize(new int[] {-1, 10})
				.build();
		assertException(input, HttpStatus.BAD_REQUEST, "Minimum valid room size is [1, 1]");
	}

	@Test
	public void throwsException_givenInvalidInstructions() {
		Input input = Input.builder()
				.roomSize(new int[] {10, 10})
				.coords(new int[] {5, 5})
				.instructions(new String("NE$W"))
				.build();
		assertException(input, HttpStatus.BAD_REQUEST, "Instruction string must only consist of characters 'N', 'E', 'S' and 'W'");
	}

	@Test
	public void throwsException_givenPatchOutOfRoomBounds() {
		List patches = new ArrayList<>();
		patches.add(new int[] {0, 0});
		patches.add(new int[] {4, 4});
		patches.add(new int[] {10, 5});

		Input input = Input.builder()
				.roomSize(new int[] {5, 5})
				.coords(new int[] {0, 0})
				.patches(patches)
				.instructions("N")
				.build();

		assertException(input, HttpStatus.BAD_REQUEST, "Patch is out of room bounds");
	}

	@Test public void throwsException_givenNullInstructions() {
		Input input = Input.builder()
				.roomSize(new int[] {10, 10})
				.coords(new int[] {5, 5})
				.build();

		assertException(input, HttpStatus.BAD_REQUEST, "Instructions string length must be greater than 0");

		input = Input.builder()
				.roomSize(new int[] {10, 10})
				.coords(new int[] {5, 5})
				.instructions("")
				.build();

		assertException(input, HttpStatus.BAD_REQUEST, "Instructions string length must be greater than 0");
	}

	@Test
	public void roombaCleansExpectedAmountOfPatches() throws RoombaException {
		List patches = new ArrayList<>();
		patches.add(new int[] {0, 0});
		patches.add(new int[] {1, 1});
		patches.add(new int[] {2, 2});
		patches.add(new int[] {2, 3});
		patches.add(new int[] {2, 4});
		patches.add(new int[] {3, 4});
		patches.add(new int[] {4, 4});
		patches.add(new int[] {4, 3});
		patches.add(new int[] {4, 2});

		Input input = Input.builder()
				.roomSize(new int[] {5, 5})
				.coords(new int[] {0, 0})
				.patches(patches)
				.instructions("NENENNEESS")
				.build();

		Output output = roombaService.processInput(input);

		assertThat(output.getPatches()).isEqualTo(9);
		assertThat(output.getCoords()[0]).isEqualTo(4);
		assertThat(output.getCoords()[1]).isEqualTo(2);
	}

	public void assertException(Input input, HttpStatus httpStatus, String message) {
		try {
			roombaService.processInput(input);
			fail();
		} catch (RoombaException ex) {
			assertThat(ex.getHttpStatus()).isEqualTo(httpStatus);
			assertThat(ex.getMessage()).isEqualTo(message);
		}
	}

}
