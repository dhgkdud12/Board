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

    private Integer parentId;
    private Integer groupNo; // 댓글별 그룹, 1부터
    private Integer layer;
    private Integer childCnt;
    private Integer groupOrd; // 계층별 댓글 그룹 순서, 0부터

}

