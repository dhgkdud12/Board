package spring.board.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    // 부모 댓글 id
    private Integer parentId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(min = 1, max = 2000, message = "댓글은 2000자 이내로 작성해주세요.")
    private String content;
}

