package spring.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BoardResponse {
    private Integer boardNo;
    private String title;
    private String content;
    private Integer userIdx;
    private String userName;
    private Timestamp createDate;
    private Timestamp updateDate;
}

