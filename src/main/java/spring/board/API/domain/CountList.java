package spring.board.API.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CountList {
    int splitTime;
    int normalCount;
    int abnormalCount;
}
