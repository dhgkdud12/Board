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
// 게시물
public class Board {

    // 게시물 번호
    private Integer boardNo;

    // 제목
    private String title;

    // 댓글 내용
    private String content;

    // 작성자
    private Integer userIdx;

    // 업로드 날짜
    private Timestamp createDate;

    // 수정 일자
    private Timestamp updateDate;

}