package io.cairncross.roomba.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("roomba")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Input {
    @Id
    private String id;
    private int[] roomSize;
    private int[] coords;
    private List<int[]> patches;
    private String instructions;
    private Output output;
}
