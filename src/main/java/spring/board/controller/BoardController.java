package spring.board.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.User;
import spring.board.domain.response.CommonResponse;
import spring.board.domain.response.ResponseStatus;
import spring.board.dto.board.BoardInfoResponse;
import spring.board.dto.board.BoardRequest;
import spring.board.dto.comment.CommentDto;
import spring.board.dto.comment.CommentRequest;
import spring.board.dto.user.UserSession;
import spring.board.service.BoardService;
import spring.board.service.CommentService;
import spring.board.service.UserService;
import spring.board.util.SessionUtils;

import javax.servlet.http.HttpServletRequest;
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
            @RequestParam(name = "keyword", required = false) String keyword) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
//        UserSession userSession = userService.getLoginUserInfo(request);
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");
        if (userSession != null) {
            System.out.println(userSession.getName()+"님 로그인중");
        }
        
        // search객체에 paging 상속받아서 search 넘겨줌
        resultMap.put("boardInfo", boardService.selectAllPosts(searchType, keyword, curPage, size, blockSize));
        resultMap.put("pageInfo", boardService.getPagingInfo(curPage, size, blockSize));

        return new CommonResponse<>(ResponseStatus.SUCCESS, "게시물 조회 성공", resultMap);
    }


    // 게시물 작성 - 파일 업로드
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse post(BoardRequest boardRequest, HttpServletRequest request) throws Exception {
        String message = boardService.post(boardRequest, request);
        return new CommonResponse<>(ResponseStatus.SUCCESS, message, null);
    }

    @GetMapping("/{bIdx}")
    public CommonResponse selectPost(@PathVariable("bIdx") Integer bIdx) {
        BoardInfoResponse boardInfo = boardService.selectPostsByPostId(bIdx);
        return new CommonResponse<>(ResponseStatus.SUCCESS, "게시물 조회 완료", boardInfo);

    }
    
    // 게시물 수정
    // 내가 작성한 게시물만 수정 가능
    @PutMapping("/{bIdx}")
    public CommonResponse updatePost(@PathVariable("bIdx") Integer bIdx, @RequestBody BoardRequest boardRequest) throws Exception {
        String message = boardService.updatePost(bIdx, boardRequest);
        return new CommonResponse<>(ResponseStatus.SUCCESS, message, null);
    }
    
    // 게시물 삭제
    // 내가 삭제한 게시물만 삭제 가능
    @DeleteMapping("/{bIdx}")
    public CommonResponse deletePost(@PathVariable("bIdx")Integer bIdx) throws Exception {
        String message = boardService.deletePost(bIdx);
        return new CommonResponse<>(ResponseStatus.SUCCESS, message, null);
    }

    // 댓글 작성
    @PostMapping("/{bIdx}")
    public CommonResponse postComment(@PathVariable("bIdx") Integer bIdx, @RequestBody CommentRequest commentRequest) throws Exception {
        String message = commentService.post(bIdx, commentRequest);
        return new CommonResponse<>(ResponseStatus.SUCCESS, message, null);
    }

    @GetMapping("/{bIdx}/comment")
    public CommonResponse selectComment(@PathVariable("bIdx") Integer bIdx) {
        List<CommentDto> comments = commentService.selectCommentsByPostId(bIdx);
        return new CommonResponse<>(ResponseStatus.SUCCESS, "댓글 조회 완료", comments);
    }

    @DeleteMapping("/{bIdx}/comment/{cIdx}")
    public CommonResponse deleteComment(@PathVariable("bIdx") Integer bIdx, @PathVariable("cIdx") Integer cIdx) throws Exception {
        String message = commentService.delete(cIdx);
        return new CommonResponse<>(ResponseStatus.SUCCESS, message, null
        );
    }

}
