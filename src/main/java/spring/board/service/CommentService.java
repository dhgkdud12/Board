package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.dao.JdbcCommentDao;
import spring.board.dto.CommentDto;
import spring.board.dto.UserSessionDto;
import spring.board.entity.Comment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    private final UserService userService;
    private final BoardService boardService;
    private final JdbcCommentDao commentDao;

    public CommentService(UserService userService, BoardService boardService, JdbcCommentDao commentDao) {
        this.userService = userService;
        this.boardService = boardService;
        this.commentDao = commentDao;
    }

    public List<Comment> selectCommentsByPostId(Integer bIdx) {
        return commentDao.selectCommentsByPostId(bIdx);
    }

    public String post(Integer bIdx, CommentDto commentDto, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("USER");

        if (userSessionDto != null && boardService.selectPostByPostId(bIdx) != null) {
            Comment comment = new Comment(null,bIdx,commentDto.getContent(), userSessionDto.getIdx(),new Timestamp(new Date().getTime()));
            System.out.println(comment);
            commentDao.insertComment(comment);
            return "댓글 작성 완료";
        } else {
            System.out.println("로그인을 먼저 해주세요.");
            return null;
        }
    }

    public String delete(Integer cIdx, HttpServletRequest request) {
        UserSessionDto userSessionDto = userService.getLoginUserInfo(request);
        Integer c_uidx = commentDao.selectCommentByCommentId(cIdx).getUserIdx();

        if (userSessionDto != null ) {
            if (c_uidx.equals(userSessionDto.getIdx())) {
                commentDao.deleteComment(cIdx);
                return "댓글 삭제 완료";
            } else {
                System.out.println("본인 댓글만 삭제 가능");
            }
        } else {
            System.out.println("사용자 로그인 정보 없음");
        }
        return "댓글 삭제 실패";
    }

    public List<Comment> selectCommentsByUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("USER");
        return commentDao.selectCommentsByUserId(userSessionDto.getIdx());
    }

    public Comment selectCommentByUserId(HttpServletRequest request, Integer cIdx) {
        HttpSession session = request.getSession();
        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("USER");
        return commentDao.selectCommentByUserId(userSessionDto.getIdx(), cIdx);
    }
}
