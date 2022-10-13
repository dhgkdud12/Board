package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.dao.JdbcTemplate.JdbcBoardDao;
import spring.board.dao.JdbcTemplate.JdbcCommentDao;
import spring.board.dao.JdbcTemplate.JdbcFileDao;
import spring.board.dao.MyBatis.BoardMapper;
import spring.board.dao.MyBatis.FileMapper;
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
    private final CommentService commentService;
    private final BoardMapper boardMapper;
//    private final JdbcBoardDao boardDao;
//    private final JdbcFileDao fileDao;
    private final FileMapper fileMapper;


    public BoardService(UserService userService, FileService fileService, CommentService commentService, JdbcBoardDao boardDao, JdbcFileDao fileDao, JdbcCommentDao commentDao, BoardMapper boardMapper, FileMapper fileMapper) {
        this.userService = userService;
        this.fileService = fileService;
        this.commentService = commentService;
//        this.boardDao = boardDao;
        this.fileMapper = fileMapper;
//        this.fileDao = fileDao;
        this.boardMapper = boardMapper;
    }



    public String post(BoardRequest boardRequest, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (userSession != null) {
            Board board = new Board(null, boardRequest.getTitle(), boardRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), null);
//            int bIdx = boardDao.insertPost(board);
            int bIdx = boardMapper.insertPost(board);
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
    public List<BoardResponse> selectAllPosts(int page, int size, int blockSize) {
        Paging paging = new Paging();
//        paging.setPaging(page, size, blockSize, boardDao.getTotalCnt());
//        return boardDao.selectPost(paging);
        paging.setPaging(page, size, blockSize, boardMapper.getTotalCnt());
        List<BoardResponse> list = boardMapper.selectPost(paging);
        return list;
    }

    public PageInfo getPagingInfo(int page, int size, int blockSize) {
        Paging paging = new Paging();
//        paging.setPaging(page, size, blockSize, boardDao.getTotalCnt());
        paging.setPaging(page, size, blockSize, boardMapper.getTotalCnt());
        return new PageInfo(paging);
    }

    public BoardResponse selectPostByPostId(Integer bIdx) {
        BoardResponse test = boardMapper.selectPostByPostId(bIdx);
//        return boardDao.selectPostByPostId(bIdx);
        return test;
    }

    public BoardInfoResponse selectPostsByPostId(Integer bIdx) {
//        FileRequest file = fileDao.selectFileByBoardId(bIdx);
        FileRequest file = fileMapper.selectFileByBoardId(bIdx);
        FileResponse fileResponse;
        if (file == null) fileResponse = null;
        else fileResponse = new FileResponse(file.getFileNo(), file.getFileName(), file.getPath());
//        return new BoardInfoResponse(boardDao.selectPostNByPostId(bIdx), fileResponse, commentService.selectCommentsByPostId(bIdx));
        return new BoardInfoResponse(boardMapper.selectPostNByPostId(bIdx), fileResponse, commentService.selectCommentsByPostId(bIdx));
    }

    public List<BoardResponse> selectPostAndCommentByPostId(Integer bIdx) {
        BoardResponse boardResponse = new BoardResponse();
        return null;
    }

    // 내 게시물 - 사용자 검색
    public List<BoardResponse> selectPostByUserId(int page, int size, int blockSize, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (userSession != null) {
            Paging paging = new Paging();
            paging.setPaging(page, size, blockSize, boardMapper.getTotalCnt());
            return boardMapper.selectPostByUserId(paging, userSession.getIdx());
        } else {
            System.out.println("로그인을 먼저 해주세요.");
            return null;
        }
    }

    public String updatePost(Integer bIdx, BoardRequest boardRequest, HttpServletRequest request) {
        UserSession userSession = userService.getLoginUserInfo(request);
        Integer b_uidx = boardMapper.selectPostByPostId(bIdx).getUserIdx();

        if (userSession != null ) {
            if (b_uidx.equals(userSession.getIdx())) {
                BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(bIdx, boardRequest.getTitle(), boardRequest.getContent(), new Timestamp(new Date().getTime()));
                boardMapper.updatePost(boardUpdateRequest);
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
        Integer b_uidx = boardMapper.selectPostByPostId(bIdx).getUserIdx();

        if (userSession != null ) {
            if (b_uidx.equals(userSession.getIdx())) {
                boardMapper.deletePost(bIdx);
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
        return boardMapper.searchPosts(q);
    }

    // 제목 검색 like 0%0
    // 내용 검색
    // 작성자 검색
}
