package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.dao.JdbcBoardDao;
import spring.board.dao.JdbcCommentDao;
import spring.board.dto.*;
import spring.board.entity.Comment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CommentService {
    private final UserService userService;
    private final JdbcBoardDao boardDao;
    private final JdbcCommentDao commentDao;

    public CommentService(UserService userService, JdbcBoardDao boardDao, JdbcCommentDao commentDao) {
        this.userService = userService;
        this.boardDao = boardDao;
        this.commentDao = commentDao;
    }

    private CommentListDto createCommentListDto(CommentResponse commentResponse) {
        List<CommentListDto> commentLayer = new ArrayList<>();
        CommentListDto curCommentListDto =
                CommentListDto.builder()
                        .commentNo(commentResponse.getCommentNo())
                        .boardNo(commentResponse.getBoardNo())
                        .content(commentResponse.getContent())
                        .userIdx(commentResponse.getUserIdx())
                        .userName(commentResponse.getUserName())
                        .date(commentResponse.getDate())
                        .parentId(commentResponse.getParentId())
                        .groupNo(commentResponse.getGroupNo())
                        .layer(commentResponse.getLayer())
                        .childCnt(commentResponse.getChildCnt())
                        .groupOrd(commentResponse.getGroupOrd())
                        .commentListDtos(commentLayer)
                        .build();
        return curCommentListDto;
    }

    private CommentListDto recursiveComment (List<CommentResponse> commentList, Integer layer, Integer commentNo, CommentListDto commentListDto) {
        for (int i = 0; i < commentList.size(); i++) {
            CommentResponse curComment = commentList.get(i);
            Integer parentId = curComment.getParentId();
            if (parentId != 0 ) {
                if (commentNo == parentId && layer == curComment.getLayer()) {
                    CommentListDto curCommentListDto = createCommentListDto(curComment);
                    commentListDto.getCommentListDtos().add(curCommentListDto);
                    if (curComment.getChildCnt() > 0) {
                        return recursiveComment(commentList, layer+1, curComment.getCommentNo(), curCommentListDto);
                    } else return commentListDto;
                }
            }
        }
        return commentListDto;
    }

    public List<CommentListDto> selectCommentsByPostId(Integer bIdx) {
        List<CommentResponse> commentList = commentDao.selectCommentsByPostId(bIdx); // 게시물에 대한 모든 댓글 정보
        List<CommentListDto> resultCommentList = new ArrayList<>();
        
        // 부모 id가 null이면 list 생성, 재귀함수 호출
        for (int i = 0; i < commentList.size(); i++) {
            CommentResponse curComment = commentList.get(i);
            if (curComment.getParentId() == 0) {
                CommentListDto commentListDto = createCommentListDto(curComment);
                if (curComment.getChildCnt() > 0) {
                    recursiveComment(commentList, 1, curComment.getCommentNo(), commentListDto);
                    resultCommentList.add(commentListDto);
                }
                else resultCommentList.add(commentListDto);
            }
        }
        
        return resultCommentList;
    }

    private List<CommentListDto> addCommentList(List<CommentListDto> parent, List<CommentListDto> child) {
        for (int i = 0; i < parent.size(); i++) {
            Integer commentId = parent.get(i).getCommentNo();
            for (int j = 0; j < child.size(); j++) {
                Integer parentId = child.get(j).getParentId();
                if (commentId == parentId) {
                    parent.get(i).getCommentListDtos().add(child.get(j));
                }
            }
        }
        return parent;
    }

    public String post(Integer bIdx, CommentRequest commentRequest, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (boardDao.selectPostByPostId(bIdx) == null) System.out.println("게시물이 존재하지 않음");
        else if (userSession != null) {
            Comment comment = null;
            if (commentRequest.getParentId() == null) {
                comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), null, null, 0, 0, 0);
            } else {
                if (commentDao.selectCommentByCommentId(commentRequest.getParentId()) == null) {
                    System.out.println("답글달 댓글이 존재하지 않음");
                    return "댓글 작성 실패";
                }
                else comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), commentRequest.getParentId(), null, 0, 0, 0);
            }

            System.out.println(commentDao.insertComment(comment));
            return "댓글 작성 완료";

//            if (commentDao.insertComment(comment) != 0) {
//                System.out.println();
//            }
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
