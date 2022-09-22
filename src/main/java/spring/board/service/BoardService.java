package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.dao.JdbcBoardDao;
import spring.board.dao.JdbcCommentDao;
import spring.board.dao.JdbcFileDao;
import spring.board.dto.*;
import spring.board.entity.Board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class BoardService {
    private final UserService userService;
    private final FileService fileService;
    private final JdbcBoardDao boardDao;
    private final JdbcFileDao fileDao;
    private final JdbcCommentDao commentDao;


    public BoardService(UserService userService, FileService fileService, JdbcBoardDao boardDao, JdbcFileDao fileDao, JdbcCommentDao commentDao) {
        this.userService = userService;
        this.fileService = fileService;
        this.boardDao = boardDao;
        this.fileDao = fileDao;
        this.commentDao = commentDao;
    }

    public String post(BoardRequest boardRequest, HttpServletRequest request) throws ParseException, IOException {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (userSession != null) {
            Board board = new Board(null, boardRequest.getTitle(), boardRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), null);
            int bIdx = boardDao.insertPost(board);
            if (boardRequest.getFile() != null) {
                boardRequest.setId(bIdx);
                return fileService.uploadFile(boardRequest, request);
            }
        } else {
            System.out.println("로그인을 먼저 해주세요.");
        }
        return null;
    }

    public List<BoardResponse> selectAllPosts(int page) {
        return boardDao.selectPost(page);
    }

    public BoardResponse selectPostByPostId(Integer bIdx) {
        return boardDao.selectPostByPostId(bIdx);
    }

    public BoardInfoResponse selectPostsByPostId(Integer bIdx) {
        FileRequest file = fileDao.selectFileByBoardId(bIdx);
        FileResponse fileResponse;
        if (file == null) fileResponse = null;
        else fileResponse = new FileResponse(file.getFileNo(), file.getFileName(), file.getPath());
        BoardInfoResponse boardInfoResponse = new BoardInfoResponse(boardDao.selectPostNByPostId(bIdx), fileResponse, commentDao.selectCommentsByPostId(bIdx));
        return boardInfoResponse;
    }

    public List<BoardResponse> selectPostAndCommentByPostId(Integer bIdx) {
        BoardResponse boardResponse = new BoardResponse();
        return null;
    }

    public List<BoardResponse> selectPostByUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (userSession != null) {
            return boardDao.selectPostByUserId(userSession.getIdx());
        } else {
            System.out.println("로그인을 먼저 해주세요.");
            return null;
        }
    }

    public String updatePost(Integer bIdx, BoardRequest boardRequest, HttpServletRequest request) {
        UserSession userSession = userService.getLoginUserInfo(request);
        Integer b_uidx = boardDao.selectPostByPostId(bIdx).getUserIdx();

        if (userSession != null ) {
            if (b_uidx.equals(userSession.getIdx())) {
                BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(bIdx, boardRequest.getTitle(), boardRequest.getContent(), new Timestamp(new Date().getTime()));
                boardDao.updatePost(boardUpdateRequest);
                return "게시물 수정 완료";
            } else {
                System.out.println("본인 게시물만 삭제 가능");
            }
        } else {
            System.out.println("사용자 로그인 정보 없음");
        }
        return "게시물 수정 실패";
    }

    public String deletePost(Integer bIdx, HttpServletRequest request) {

        UserSession userSession = userService.getLoginUserInfo(request);
        Integer b_uidx = boardDao.selectPostByPostId(bIdx).getUserIdx();

        if (userSession != null ) {
            if (b_uidx.equals(userSession.getIdx())) {
                boardDao.deletePost(bIdx);
                return "게시물 삭제 완료";
            } else {
                System.out.println("본인 게시물만 삭제 가능");
            }
        } else {
            System.out.println("사용자 로그인 정보 없음");
        }
        return "게시물 삭제 실패";
    }
}
