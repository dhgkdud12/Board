package spring.board.API.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RmcOutDto {
    String rmcOutStDate;
    String rmcOutEnDate;
    String carInDate;
    String carOutDate;
}
