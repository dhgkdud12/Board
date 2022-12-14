package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.common.response.CommonResponse;
import spring.board.common.response.ResponseStatus;
import spring.board.common.response.exception.ErrorCode;
import spring.board.common.response.exception.TicketingException;
import spring.board.common.response.SuccessMessage;
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
import spring.board.service.file.FTPFileService;
import spring.board.util.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
//@RequiredArgsConstructor
public class BoardService {
    private final UserService userService;
    private final FTPFileService fileService;
    private final CommentService commentService;
    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;

    //    private final JdbcBoardDao boardDao;
//    private final JdbcFileDao fileDao;

    public BoardService(UserService userService, FTPFileService fileService, CommentService commentService, JdbcBoardDao boardDao, JdbcFileDao fileDao, JdbcCommentDao commentDao, BoardMapper boardMapper, FileMapper fileMapper) {
        this.userService = userService;
        this.fileService = fileService;
        this.commentService = commentService;
        this.fileMapper = fileMapper;
        this.boardMapper = boardMapper;
//        this.boardDao = boardDao;
//        this.fileDao = fileDao;

    }

    public CommonResponse post(BoardRequest boardRequest, HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        UserSession userSession = (UserSession) session.getAttribute("USER");

        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");

        if (userSession != null) {
            Board board = new Board(null, boardRequest.getTitle(), boardRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), null);
//            int bIdx = boardDao.insertPost(board);
            boardMapper.insertPost(board);
            boardRequest.setBId(board.getBoardNo());

            Integer bIdx = boardRequest.getBId();
            if (boardRequest.getFile() != null) {
                try {
                    fileService.uploadFiletoFtp(boardRequest); // ?????? ?????????
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new TicketingException(ErrorCode.INVALID_LOGIN);
        }
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "????????? " + SuccessMessage.SUCCESS_CREATE.getMessage(), null);
    }

    // ?????????
    public List<BoardResponse> selectAllPosts(String searchType, String keyword, int curPage, int pageSize, int blockSize) {
        BoardSearchRequest search = new BoardSearchRequest(searchType, keyword, curPage, pageSize, blockSize, boardMapper.getTotalCnt());
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

    public BoardInfoResponse selectPostByPostId(Integer bIdx) {
//        FileRequest file = fileDao.selectFileByBoardId(bIdx);
        FileRequest file = fileMapper.selectFileByBoardId(bIdx);
        FileResponse fileResponse;
        if (file == null) fileResponse = null;
        else fileResponse = new FileResponse(file.getFileNo(), file.getFileName(), file.getPath()); // null
//        return new BoardInfoResponse(boardDao.selectPostNByPostId(bIdx), fileResponse, commentService.selectCommentsByPostId(bIdx));

        BoardInfoResponse boardInfo =
                new BoardInfoResponse(
                        boardMapper.selectPostByPostId(bIdx),
                        fileResponse,
                        commentService.selectCommentsByPostId(bIdx));

        return boardInfo;
    }

    // ??? ????????? - ????????? ??????
    public List<BoardResponse> selectPostsByUserId(int page, int size, int blockSize) {
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");

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
            throw new TicketingException(ErrorCode.INVALID_LOGIN);
        }
    }

    public CommonResponse updatePost(int bIdx, BoardRequest boardRequest) {
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");

        if (boardMapper.selectPostByPostId(bIdx) == null) {
            throw new TicketingException(ErrorCode.INVALID_BOARD);
        }

        Integer b_uidx = boardMapper.selectPostByPostId(bIdx).getUserIdx();

        if (userSession == null ) {
            throw new TicketingException(ErrorCode.INVALID_LOGIN);
        } else {
            if (!b_uidx.equals(userSession.getIdx())) {
                throw new TicketingException(ErrorCode.INVALID_USER);
            } else {
                BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(bIdx, boardRequest.getTitle(), boardRequest.getContent(), new Timestamp(new Date().getTime()));
                boardMapper.updatePost(boardUpdateRequest);
                return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "????????? " + SuccessMessage.SUCCESS_UPDATE.getMessage(), null);
            }
        }
    }

    public CommonResponse deletePost(int bIdx) {

        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");

        if (boardMapper.selectPostByPostId(bIdx) == null) {
            throw new TicketingException(ErrorCode.INVALID_BOARD);
        }

        Integer b_uidx = boardMapper.selectPostByPostId(bIdx).getUserIdx();

        if (userSession == null ) {
            throw new TicketingException(ErrorCode.INVALID_LOGIN);
        }
        else {
            if (!b_uidx.equals(userSession.getIdx())) {
                throw new TicketingException(ErrorCode.INVALID_USER);
            } else {
                boardMapper.deletePost(bIdx);
                return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "????????? " + SuccessMessage.SUCCESS_DELETE.getMessage(), null);
            }
        }
    }
}
