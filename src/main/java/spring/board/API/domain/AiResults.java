package spring.board.API.domain;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class AiResults {
    String carInDate;
    String carOutDate;
    List<DataList> dataList;
}
