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

    // 부모 댓글 ID 번호
//    private Integer originCoNo;

    // 그룹 내 번호 - 댓글 1, 댓글 2, 댓글 3
//    private Integer groupNo;

    // 댓글 계층 댓글, 대댓글, 대댓글의 댓글
//    private Integer layer;

    // 자식 댓글 수
//    private Integer childCoCnt;

    // 댓글 그룹들의 순서
//    private Integer groupOrder;

}

