package spring.board.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import spring.board.common.response.CommonResponse;
import spring.board.common.response.SuccessMessage;
import spring.board.common.response.ResponseStatus;
import spring.board.dto.board.BoardInfoResponse;
import spring.board.dto.board.BoardRequest;
import spring.board.dto.comment.CommentDto;
import spring.board.dto.comment.CommentRequest;
import spring.board.dto.user.UserSession;
import spring.board.service.BoardService;
import spring.board.service.CommentService;
import spring.board.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("board")
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;
    private final CommentService commentService;

    public BoardController(BoardService boardService, UserService userService, CommentService commentService) {
        this.boardService = boardService;
        this.userService = userService;
        this.commentService = commentService;
    }

    // 홈화면
    @GetMapping("")
    public CommonResponse home(
            @RequestParam(name = "page", required = false, defaultValue = "1") int curPage,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "blockSize", required = false, defaultValue = "5") int blockSize,
            @RequestParam(name = "searchType", required = false, defaultValue = "title") String searchType,
            @RequestParam(name = "keyword", required = false) String keyword) {
        Map<String, Object> resultMap = new HashMap<>();
        UserSession userSession = userService.getLoginUserInfo();
        if (userSession != null) {
            System.out.println(userSession.getName()+"님 로그인중");
        }
        
        resultMap.put("boardInfo", boardService.selectAllPosts(searchType, keyword, curPage, size, blockSize));
        resultMap.put("pageInfo", boardService.getPagingInfo(curPage, size, blockSize));

        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_READ.getMessage(), resultMap);
    }


    // 게시물 작성
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse post(BoardRequest boardRequest, HttpServletRequest request) throws IOException {
        String message = boardService.post(boardRequest, request);
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, message, null);
    }

    @GetMapping("/{bIdx}")
    public CommonResponse selectPost(@PathVariable("bIdx") Integer bIdx) {
        BoardInfoResponse boardInfo = boardService.selectPostByPostId(bIdx);
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_READ.getMessage(), boardInfo);

    }
    
    // 게시물 수정
    // 내가 작성한 게시물만 수정 가능
    @PutMapping("/{bIdx}")
    public CommonResponse updatePost(@PathVariable("bIdx") Integer bIdx, @RequestBody BoardRequest boardRequest) {
        String message = boardService.updatePost(bIdx, boardRequest);
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, message, null);
    }
    
    // 게시물 삭제
    // 내가 삭제한 게시물만 삭제 가능
    @DeleteMapping("/{bIdx}")
    public CommonResponse deletePost(@PathVariable("bIdx")Integer bIdx) {
        String message = boardService.deletePost(bIdx);
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, message, null);
    }

    // 댓글 작성
    @PostMapping("/{bIdx}")
    public CommonResponse postComment(@PathVariable("bIdx") Integer bIdx, @RequestBody CommentRequest commentRequest) throws Exception {
        String message = commentService.post(bIdx, commentRequest);
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, message, null);
    }

    @GetMapping("/{bIdx}/comment")
    public CommonResponse selectComment(@PathVariable("bIdx") Integer bIdx) {
        List<CommentDto> comments = commentService.selectCommentsByPostId(bIdx);
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_READ.getMessage(), comments);
    }

    @DeleteMapping("/{bIdx}/comment/{cIdx}")
    public CommonResponse deleteComment(@PathVariable("bIdx") Integer bIdx, @PathVariable("cIdx") Integer cIdx) throws Exception {
        String message = commentService.delete(cIdx);
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, message, null
        );
    }

}
