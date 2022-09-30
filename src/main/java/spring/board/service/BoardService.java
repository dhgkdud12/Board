package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.dao.JdbcBoardDao;
import spring.board.dao.JdbcCommentDao;
import spring.board.dao.JdbcFileDao;
import spring.board.dto.*;
import spring.board.entity.Board;
import spring.board.entity.Paging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
//@RequiredArgsConstructor
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



    public String post(BoardRequest boardRequest, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (userSession != null) {
            Board board = new Board(null, boardRequest.getTitle(), boardRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), null);
            int bIdx = boardDao.insertPost(board);
            if (boardRequest.getFile() != null) {
                boardRequest.setId(bIdx);
                return fileService.uploadFiletoFtp(boardRequest, request); // 파일 업로드
            }
        } else {
            System.out.println("로그인을 먼저 해주세요.");
        }
        return null;
    }

    // 페이지
    public List<BoardResponse> selectAllPosts(int page, int size) {
        Paging paging = new Paging();
        if (page == 1) paging.setTotalCnt(boardDao.getTotalCnt());
        paging.setPaging(page, size);
        return boardDao.selectPost(paging);
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

    // 내 게시물 - 사용자 검색
    public List<BoardResponse> selectPostByUserId(int page, int size, HttpServletRequest request) {
        // 총 개수 가져와서 페이지정보 설정
        // 인덱스 1부터 10개만 출력
        // db에 startIndex 넘겨줌
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (userSession != null) {
            Paging paging = new Paging();
            if (page == 1) paging.setTotalCnt(boardDao.getTotalCnt());
            paging.setPaging(page, size);
            return boardDao.selectPostByUserId(paging, userSession.getIdx());
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

    public BoardResponse searchPosts(String q) {
        return boardDao.searchPosts(q);
    }

    // 제목 검색 like 0%0
    // 내용 검색
    // 작성자 검색
}
