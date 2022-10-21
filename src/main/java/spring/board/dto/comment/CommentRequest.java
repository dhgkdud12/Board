package spring.board.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    // 부모 댓글 id
    private Integer parentId;
    private String content;
}

