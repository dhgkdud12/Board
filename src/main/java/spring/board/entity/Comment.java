package spring.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    // 댓글 번호
    private Integer commentNo;

    // 게시물 번호
    private Integer boardNo;

    // 댓글 내용
    private String content;

    // 작성자
    private Integer userIdx;

    // 작성 일자
    private Timestamp date;

}
