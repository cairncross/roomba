package io.cairncross.roomba.service;

import io.cairncross.roomba.common.RoombaException;
import io.cairncross.roomba.model.Input;
import io.cairncross.roomba.model.Output;
import io.cairncross.roomba.repository.RoombaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class RoombaServiceImpl implements RoombaService {

    @Autowired
    private RoombaRepository roombaRepository;

    @Override
    public Output processInput(Input input) throws RoombaException {

        validateRoomSize(input.getRoomSize());
        validateCoords(input.getCoords(), input.getRoomSize());
        validateInstructions(input.getInstructions());
        int[][] room = initializeRoom(input.getRoomSize(), input.getPatches());
//        int rows = input.getRoomSize()[1];
//        int columns = input.getRoomSize()[0];
//        boolean[][] room = new boolean[rows][columns];
//        for (boolean[] row: room) Arrays.fill(row, false);
//        List<int[]> moves = generateMoves(input.getCoords(), input.getInstructions());
        Stack<int[]> moves = generateMoves(input.getCoords(), input.getInstructions(), input.getRoomSize());
//        Stack<int[]> moves = new Stack<>();
//        moves.push(input.getCoords());
//        for (char direction: input.getInstructions().toCharArray()) {
//            int[] prevPosition = moves.peek();
//            int[] newPosition = move(prevPosition, direction, input.getRoomSize());
//            moves.push(newPosition);
//            System.out.println(newPosition[0] + " " + newPosition[1]);
//        }
        int cleanedPatches = 0;
        for (int[] patch: input.getPatches()) {
            for (int[] move: moves) {
                if (Arrays.equals(patch, move)) {
                    cleanedPatches++;
                    break;
                }
            }
        }
        Output output = Output.builder()
                .coords(moves.pop())
                .patches(cleanedPatches)
                .build();
        input.setOutput(output);
        Output savedOutput = roombaRepository.save(input).getOutput();
        String str = "";
        System.out.println("print map");
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                str += Integer.toString(room[i][j]) + " ";
            }
            System.out.println(str + "\n");
            str = "";
        }
        return savedOutput;
    }

    public void validateInstructions(String instructions) throws RoombaException {

        if (instructions == null || instructions.length() == 0) {
            throw new RoombaException(HttpStatus.BAD_REQUEST, "Instructions string length must be greater than 0");
        }
        for (char c: instructions.toUpperCase().toCharArray()) {
            if (c != 'N' && c != 'E' && c != 'S' && c != 'W') {
                throw new RoombaException(HttpStatus.BAD_REQUEST, "Instruction string must only consist of characters 'N', 'E', 'S' and 'W'");
            }
        }
    }

    public void validateCoords(int[] coords, int[] roomSize) throws RoombaException {
        if (coords[0] < 0 || coords[1] < 0 || coords[0] > roomSize[0] - 1 || coords[1] > roomSize[1] - 1) {
            throw new RoombaException(HttpStatus.BAD_REQUEST, "Invalid input coords");
        }
    }

    public void validateRoomSize(int[] roomSize) throws RoombaException {
        if (roomSize[0] < 1 || roomSize[1] < 1) {
            throw new RoombaException(HttpStatus.BAD_REQUEST, "Minimum valid room size is [1, 1]");
        }
    }

    public int[][] initializeRoom(int[] roomSize, List<int[]> patches) throws RoombaException {
        int rows = roomSize[1];
        int columns = roomSize[0];
        int[][] room = new int[rows][columns];
        for (int[] row: room) Arrays.fill(row, 0);
        for (int[] patch: patches) {
            if (patch[0] > roomSize[0] - 1 || patch[1] > roomSize[1] - 1){
                throw new RoombaException(HttpStatus.BAD_REQUEST, "Patch is out of room bounds");
            }
            room[patch[1]][patch[0]] = 1;
        }
        return room;
    }

    public Stack<int[]> generateMoves(int[] coords, String instructions, int[] roomSize) {
        System.out.println("print moves");
        Stack<int[]> moves = new Stack<>();
        moves.push(coords);
        for (char direction: instructions.toCharArray()) {
            int[] prevPosition = moves.peek();
            int[] newPosition = move(prevPosition, direction, roomSize);
            moves.push(newPosition);
            System.out.println(newPosition[0] + " " + newPosition[1]);
        }
        return moves;
    }

    public int[] move(int[] prevPosition, char direction, int[] roomSize) {
        int[] newPosition = Arrays.copyOf(prevPosition, 2);
        switch (direction) {
            case 'N':
                newPosition[0] = prevPosition[0];
                newPosition[1] = prevPosition[1] + 1;
                break;
            case 'E':
                newPosition[0] = prevPosition[0] + 1;
                newPosition[1] = prevPosition[1];
                break;
            case 'S':
                newPosition[0] = prevPosition[0];
                newPosition[1] = prevPosition[1] - 1;
                break;
            case 'W':
                newPosition[0] = prevPosition[0] - 1;
                newPosition[1] = prevPosition[1];
                break;
        }
        if (newPosition[0] < 0 || newPosition[1] < 0 || newPosition[0] > roomSize[0] - 1 || newPosition[1] > roomSize[1] - 1) {
            return prevPosition;
        }
        return newPosition;
    }
}
