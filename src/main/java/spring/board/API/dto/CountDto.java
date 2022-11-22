package spring.board.API.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CountDto {
    Integer idx;
    int splitTime;
    int normalCount;
    int abnormalCount;
    String carInDate;
    String carOutDate;
    String rmcOutStDate;
    String rmcOutEnDate;
}
