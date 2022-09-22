package spring.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateRequest {
    private Integer boardNo;
    private String title;
    private String content;
    private Timestamp updateTime;
}

