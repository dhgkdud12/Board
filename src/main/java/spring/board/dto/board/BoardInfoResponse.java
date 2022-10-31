package spring.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.board.dto.comment.CommentDto;
import spring.board.dto.file.FileResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardInfoResponse {
    private BoardResponse boardResponse;
    private FileResponse fileResponse;
//    private List<CommentResponse> commentDtos;
    private List<CommentDto> commentDtos;
}

