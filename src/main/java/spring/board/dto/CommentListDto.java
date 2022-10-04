package spring.board.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentListDto {
    private Integer commentNo;
    private Integer boardNo;
    private String content;
    private Integer userIdx;
    private String userName;
    private Timestamp date;

    private Integer parentId;
    private Integer groupNo;
    private Integer layer;
    private Integer childCnt;
    private Integer groupOrd;

    private List<CommentListDto> commentListDtos;
}
