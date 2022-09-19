package spring.board.controller;

import org.springframework.web.bind.annotation.*;
import spring.board.dto.BoardDto;
import spring.board.dto.CommentDto;
import spring.board.dto.UserSessionDto;
import spring.board.entity.Board;
import spring.board.entity.Comment;
import spring.board.service.BoardService;
import spring.board.service.CommentService;
import spring.board.service.UserService;

import javax.servlet.http.HttpServletRequest;
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
    public List<Board> home(HttpServletRequest request) {
        UserSessionDto userSessionDto = userService.getLoginUserInfo(request);
        if (userSessionDto != null) {
            System.out.println(userSessionDto.getName()+"님 로그인중");
        }
        return boardService.selectAllPosts();
    }


    // 게시물 작성
    // 파일 처리
    @PostMapping("")
    public String post(@RequestBody BoardDto boardDto, HttpServletRequest request) {
        return boardService.post(boardDto, request);
    }

    @GetMapping("/{bIdx}")
    public Board selectPost(@PathVariable("bIdx") Integer bIdx) {
        return boardService.selectPostByPostId(bIdx);
    }
    
    // 게시물 수정
    // 내가 작성한 게시물만 수정 가능
    @PutMapping("/{bIdx}")
    public String updatePost(@PathVariable("bIdx") Integer bIdx, @RequestBody BoardDto boardDto, HttpServletRequest request) {
        return boardService.updatePost(bIdx, boardDto, request);
    }
    
    // 게시물 삭제
    // 내가 삭제한 게시물만 삭제 가능
    @DeleteMapping("/{bIdx}")
    public String deletePost(@PathVariable("bIdx")Integer bIdx, HttpServletRequest request) {
        return boardService.deletePost(bIdx, request);
    }

    // 댓글 작성
    @PostMapping("/{bIdx}")
    public String postComment(@PathVariable("bIdx") Integer bIdx, @RequestBody CommentDto commentDto, HttpServletRequest request) {
        return commentService.post(bIdx, commentDto, request);
    }

    @GetMapping("/{bIdx}/comment/{cIdx}")
    public List<Comment> selectComment(@PathVariable("bIdx") Integer bIdx, @PathVariable("cIdx") Integer cIdx) {
        return commentService.selectCommentsByPostId(bIdx);
    }

    @DeleteMapping("/{bIdx}/comment/{cIdx}")
    public String deleteComment(@PathVariable("bIdx") Integer bIdx, @PathVariable("cIdx") Integer cIdx, HttpServletRequest request) {
        return commentService.delete(bIdx, request);
    }
}
