package spring.board.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import spring.board.dto.*;
import spring.board.service.BoardService;
import spring.board.service.CommentService;
import spring.board.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
    public List<BoardResponse> home(@RequestParam(name = "page", required = false) int page, @RequestParam(name = "size", value = "10", required = false) int size, HttpServletRequest request) {
        UserSession userSession = userService.getLoginUserInfo(request);
        if (userSession != null) {
            System.out.println(userSession.getName()+"님 로그인중");
        }
        return boardService.selectAllPosts(page, size);
    }


    // 게시물 작성
    // 파일 처리
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String post(BoardRequest boardRequest, HttpServletRequest request) throws IOException, ParseException {
        return boardService.post(boardRequest, request);
    }

    @GetMapping("/{bIdx}")
    public BoardInfoResponse selectPost(@PathVariable("bIdx") Integer bIdx) {
        return boardService.selectPostsByPostId(bIdx);
    }
    
    // 게시물 수정
    // 내가 작성한 게시물만 수정 가능
    @PutMapping("/{bIdx}")
    public String updatePost(@PathVariable("bIdx") Integer bIdx, @RequestBody BoardRequest boardRequest, HttpServletRequest request) {
        return boardService.updatePost(bIdx, boardRequest, request);
    }
    
    // 게시물 삭제
    // 내가 삭제한 게시물만 삭제 가능
    @DeleteMapping("/{bIdx}")
    public String deletePost(@PathVariable("bIdx")Integer bIdx, HttpServletRequest request) {
        return boardService.deletePost(bIdx, request);
    }

    // 댓글 작성
    @PostMapping("/{bIdx}")
    public String postComment(@PathVariable("bIdx") Integer bIdx, @RequestBody CommentRequest commentRequest, HttpServletRequest request) {
        return commentService.post(bIdx, commentRequest, request);
    }

    @GetMapping("/{bIdx}/comment/{cIdx}")
    public List<CommentListDto> selectComment(@PathVariable("bIdx") Integer bIdx) {
        return commentService.selectCommentsByPostId(bIdx);
    }

    @DeleteMapping("/{bIdx}/comment/{cIdx}")
    public String deleteComment(@PathVariable("bIdx") Integer bIdx, @PathVariable("cIdx") Integer cIdx, HttpServletRequest request) {
        return commentService.delete(cIdx, request);
    }
}
