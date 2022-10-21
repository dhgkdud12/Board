package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.dao.JdbcTemplate.JdbcBoardDao;
import spring.board.dao.JdbcTemplate.JdbcCommentDao;
import spring.board.dao.JdbcTemplate.JdbcFileDao;
import spring.board.dao.MyBatis.BoardMapper;
import spring.board.dao.MyBatis.FileMapper;
import spring.board.domain.Board;
import spring.board.domain.Paging;
import spring.board.dto.board.*;
import spring.board.dto.common.PageInfo;
import spring.board.dto.file.FileRequest;
import spring.board.dto.file.FileResponse;
import spring.board.dto.user.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
//@RequiredArgsConstructor
public class BoardService {
    private final UserService userService;
    private final FileService fileService;
    private final CommentService commentService;
    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;

    //    private final JdbcBoardDao boardDao;
//    private final JdbcFileDao fileDao;

    public BoardService(UserService userService, FileService fileService, CommentService commentService, JdbcBoardDao boardDao, JdbcFileDao fileDao, JdbcCommentDao commentDao, BoardMapper boardMapper, FileMapper fileMapper) {
        this.userService = userService;
        this.fileService = fileService;
        this.commentService = commentService;
        this.fileMapper = fileMapper;
        this.boardMapper = boardMapper;
//        this.boardDao = boardDao;
//        this.fileDao = fileDao;

    }



    public String post(BoardRequest boardRequest, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (userSession != null) {
            Board board = new Board(null, boardRequest.getTitle(), boardRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), null);
//            int bIdx = boardDao.insertPost(board);
            Integer bIdx = boardMapper.insertPost(board);

            if (bIdx == null) {
                return "게시물 작성 실패";
            } else {
                if (boardRequest.getFile() != null) {
                    boardRequest.setId(bIdx);
                    fileService.uploadFiletoFtp(boardRequest, request); // 파일 업로드
                }
            }
        } else {
            System.out.println("로그인을 먼저 해주세요.");
            return "게시물 작성 실패";
        }
        return "게시물 작성 완료";
    }

    // 페이지
    public List<BoardResponse> selectAllPosts(int curPage, int pageSize, int blockSize) {
        BoardSearchRequest search = new BoardSearchRequest(curPage, pageSize, blockSize, boardMapper.getTotalCnt());
//        return boardDao.selectPost(paging);
        List<BoardResponse> list = boardMapper.selectPost(search);
        return list;
    }

    public PageInfo getPagingInfo(int page, int size, int blockSize) {
        Paging paging = new Paging(page, size, blockSize, boardMapper.getTotalCnt());
//        paging.setPaging(page, size, blockSize, boardDao.getTotalCnt());
        PageInfo pageInfo = new PageInfo(paging);
        return pageInfo;
    }

    public BoardResponse selectPostByPostId(Integer bIdx) {
        BoardResponse boardResponse = boardMapper.selectPostByPostId(bIdx);
        return boardResponse;
    }

    public BoardInfoResponse selectPostsByPostId(Integer bIdx) {
//        FileRequest file = fileDao.selectFileByBoardId(bIdx);
        FileRequest file = fileMapper.selectFileByBoardId(bIdx);
        FileResponse fileResponse;
        if (file == null) fileResponse = null;
        else fileResponse = new FileResponse(file.getFileNo(), file.getFileName(), file.getPath());
//        return new BoardInfoResponse(boardDao.selectPostNByPostId(bIdx), fileResponse, commentService.selectCommentsByPostId(bIdx));

        BoardInfoResponse boardInfoResponse =
                new BoardInfoResponse(
                        boardMapper.selectPostByPostId(bIdx),
                        fileResponse,
                        commentService.selectCommentsByPostId(bIdx));

        return boardInfoResponse;
    }

    public List<BoardResponse> selectPostAndCommentByPostId(Integer bIdx) {
        BoardResponse boardResponse = new BoardResponse();
        return null;
    }

    // 내 게시물 - 사용자 검색
    public List<BoardResponse> selectPostsByUserId(int page, int size, int blockSize, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (userSession != null) {
            Paging paging = new Paging(page, size, blockSize, boardMapper.getTotalCnt());
//            paging.setPaging(page, size, blockSize, boardMapper.getTotalCnt());
            Map<String, Integer> map = new HashMap<>();
            map.put("startIndex", paging.getStartIndex());
            map.put("endIndex", paging.getEndIndex());
            map.put("id", userSession.getIdx());
            List<BoardResponse> list = boardMapper.selectPostsByUserId(map);
            return list;
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
