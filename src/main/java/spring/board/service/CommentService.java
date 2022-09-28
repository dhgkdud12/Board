package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.dao.JdbcCommentDao;
import spring.board.dto.CommentRequest;
import spring.board.dto.CommentResponse;
import spring.board.dto.UserSession;
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

    public List<CommentResponse> selectCommentsByPostId(Integer bIdx) {
        return commentDao.selectCommentsByPostId(bIdx);
    }

    public String post(Integer bIdx, CommentRequest commentRequest, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (boardService.selectPostByPostId(bIdx) == null) System.out.println("게시물이 존재하지 않음");
        else if (userSession != null) {

            // 부모 id가 없으면 댓글
            // parentCIdx는 null, groupNo 자동증가, layer 0, child 0, 0
            // 부모 id가 있으면 대댓글
            // parentCIdx, groupNo 그대로, layer 가져와서 +1,  자식 댓글 수 0, 부모의 자식 댓글 수 +1, 부모의 groupOrder+1

            Comment comment = new Comment(null,bIdx, commentRequest.getContent(), userSession.getIdx(),new Timestamp(new Date().getTime()));
            commentDao.insertComment(comment);
            return "댓글 작성 완료";
        } else {
            System.out.println("로그인을 먼저 해주세요.");
        }
        return "댓글 작성 실패";
    }

    public String delete(Integer cIdx, HttpServletRequest request) { // 글이 존재하지 않을 경우
        UserSession userSession = userService.getLoginUserInfo(request);
        Integer c_uidx = commentDao.selectCommentByCommentId(cIdx).getUserIdx();

        if (c_uidx == null) System.out.println("게시물이 존재하지 않음");
        else if (userSession != null ) {
            if (c_uidx.equals(userSession.getIdx())) {
                commentDao.deleteComment(cIdx);
                return "댓글 삭제 완료";
            } else {
                System.out.println("본인 댓글만 삭제 가능");
            }
        } else {
            System.out.println("로그인 후 댓글 작성 가능");
        }
        return "댓글 삭제 실패";
    }

    public List<CommentResponse> selectCommentsByUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");
        return commentDao.selectCommentsByUserId(userSession.getIdx());
    }

    public CommentResponse selectCommentByUserId(HttpServletRequest request, Integer cIdx) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");
        return commentDao.selectCommentByUserId(userSession.getIdx(), cIdx);
    }
}
