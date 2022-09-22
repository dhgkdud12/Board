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
public class CommentResponse {
    private Integer commentNo;
    private Integer boardNo;
    private String content;
    private Integer userIdx;
    private String userName;
    private Timestamp date;
}

