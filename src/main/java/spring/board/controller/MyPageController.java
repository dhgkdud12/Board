package spring.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.board.common.response.CommonResponse;
import spring.board.common.response.SuccessMessage;
import spring.board.common.response.ResponseStatus;
import spring.board.dto.comment.CommentResponse;
import spring.board.service.BoardService;
import spring.board.service.CommentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/myPage")
public class MyPageController {

    private final BoardService boardService;
    private final CommentService commentService;


    public MyPageController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    // 내가 작성한 글
    @GetMapping("/boards")
    public CommonResponse myBoards(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "blockSize",  required = false, defaultValue = "10") int blockSize) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("boardInfo", boardService.selectPostsByUserId(page, size, blockSize));
        resultMap.put("pageInfo", boardService.getPagingInfo(page, size, blockSize));
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_READ.getMessage(), resultMap);
    }
    
    // 내가 작성한 댓글
    @GetMapping("/comments")
    public CommonResponse myComments() {
        List<CommentResponse> comments = commentService.selectCommentsByUserId();
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_READ.getMessage(), comments);
    }

}
