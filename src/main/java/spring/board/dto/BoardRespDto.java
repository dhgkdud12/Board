package spring.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.board.entity.Comment;

import java.sql.Timestamp;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardRespDto {
    private Integer boardNo;
    private String title;
    private String content;
    private String bName;
    private Timestamp bDate;
    private Timestamp bUdate;
    private List<Comment> comment;
}

