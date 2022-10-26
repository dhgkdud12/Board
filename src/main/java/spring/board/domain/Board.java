package spring.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    private Integer boardNo;
    private String title;
    private String content;
    private Integer userIdx;
    private Timestamp createDate;
    private Timestamp updateDate;

}