package spring.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardInfoResponse {
    private BoardResponse boardResponse;
    private FileResponse fileResponse;
    private List<CommentResponse> commentResponses;
}
