package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.dao.JdbcBoardDao;
import spring.board.dto.BoardDto;
import spring.board.dto.BoardRespDto;
import spring.board.dto.UpdateBoardDto;
import spring.board.dto.UserSessionDto;
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

    public BoardService(UserService userService, FileService fileService, JdbcBoardDao boardDao) {
        this.userService = userService;
        this.fileService = fileService;
        this.boardDao = boardDao;
    }

    public String post(BoardDto boardDto, HttpServletRequest request) throws ParseException, IOException {
        HttpSession session = request.getSession();
        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("USER");

        if (userSessionDto != null) {
            Board board = new Board(null,boardDto.getTitle(),boardDto.getContent(),userSessionDto.getIdx(), new Timestamp(new Date().getTime()), null);
            int bIdx = boardDao.insertPost(board);

            if (boardDto.getFile() != null) {
                boardDto.setId(bIdx);
                return fileService.uploadFile(boardDto, request);
            }
        } else {
            System.out.println("로그인을 먼저 해주세요.");
        }
        return null;
    }

    public List<Board> selectAllPosts() {
        return boardDao.selectPost();
    }

    public Board selectPostByPostId(Integer bIdx) {
        return boardDao.selectPostByPostId(bIdx);
    }

    public List<BoardRespDto> selectPostAndCommentByPostId(Integer bIdx) {
        BoardRespDto boardRespDto = new BoardRespDto();
        return null;
    }

    public List<Board> selectPostByUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("USER");

        if (userSessionDto != null) {
            return boardDao.selectPostByUserId(userSessionDto.getIdx());
        } else {
            System.out.println("로그인을 먼저 해주세요.");
            return null;
        }
    }

    public String updatePost(Integer bIdx, BoardDto boardDto, HttpServletRequest request) {
        UserSessionDto userSessionDto = userService.getLoginUserInfo(request);
        Integer b_uidx = boardDao.selectPostByPostId(bIdx).getUserIdx();

        if (userSessionDto != null ) {
            if (b_uidx.equals(userSessionDto.getIdx())) {
                UpdateBoardDto updateBoardDto = new UpdateBoardDto(bIdx,boardDto.getTitle(),boardDto.getContent(), new Timestamp(new Date().getTime()));
                boardDao.updatePost(updateBoardDto);
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

        UserSessionDto userSessionDto = userService.getLoginUserInfo(request);
        Integer b_uidx = boardDao.selectPostByPostId(bIdx).getUserIdx();

        if (userSessionDto != null ) {
            if (b_uidx.equals(userSessionDto.getIdx())) {
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
