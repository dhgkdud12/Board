package spring.board.API.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class DataList {
    String rmcOutStDate;
    String rmcOutEnDate;
    int splitTime;
    List<CountList> countList;
}
