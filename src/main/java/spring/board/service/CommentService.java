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

    public List<CommentListDto> selectCommentsByPostId(Integer bIdx) {
        List<CommentResponse> commentList = commentDao.selectCommentsByPostId(bIdx);
        System.out.println("layer 0");
        
        // layer가 0인 댓글의 개수 가져와서 그만큼 arraylist 배열 만들고
        // layer 1 넣어줌
        HashMap<Integer, CommentListDto> hashMap = new HashMap<>();
        for (int i = 0; i < commentList.size(); i++) {
            if (commentList.get(i).getLayer() == 0) {
                CommentResponse comment = commentList.get(i);
                List<CommentListDto> commentLayer0 = new ArrayList<>();
                CommentListDto commentListDto =
                        CommentListDto.builder()
                                .commentNo(comment.getCommentNo())
                                .boardNo(comment.getBoardNo())
                                .content(comment.getContent())
                                .userIdx(comment.getUserIdx())
                                .userName(comment.getUserName())
                                .date(comment.getDate())
                                .parentId(comment.getParentId())
                                .groupNo(comment.getGroupNo())
                                .layer(comment.getLayer())
                                .childCnt(comment.getChildCnt())
                                .groupOrd(comment.getGroupOrd())
                                .commentListDtos(commentLayer0)
                                .build();
                hashMap.put(commentList.get(i).getCommentNo(), commentListDto);
            }
        }

        System.out.println("layer 1");
        for (int i = 0; i < commentList.size(); i++) {
            if (commentList.get(i).getLayer() == 1) {
                CommentListDto commentListDtoOrin = hashMap.get(commentList.get(i).getParentId());
                List<CommentListDto> commentLayer1 = new ArrayList<>();
                CommentListDto commentListDto =
                        CommentListDto.builder()
                                .commentNo(commentList.get(i).getCommentNo())
                                .boardNo(commentList.get(i).getBoardNo())
                                .content(commentList.get(i).getContent())
                                .userIdx(commentList.get(i).getUserIdx())
                                .userName(commentList.get(i).getUserName())
                                .date(commentList.get(i).getDate())
                                .parentId(commentList.get(i).getParentId())
                                .groupNo(commentList.get(i).getGroupNo())
                                .layer(commentList.get(i).getLayer())
                                .childCnt(commentList.get(i).getChildCnt())
                                .groupOrd(commentList.get(i).getGroupOrd())
                                .commentListDtos(commentLayer1)
                                .build();
                commentListDtoOrin.getCommentListDtos().add(commentListDto);
            }
        }

        System.out.println("layer 2");
        for (int i = 1; i < commentList.size()+1; i++) {
            if (commentList.get(i).getLayer() == 2) { // layer 2의 parentId 값이 comment_no인 List에 layer 2값 데이터 추가
                System.out.println(hashMap.get(i).getCommentListDtos().size());
                for (int j = 0; j < hashMap.get(i).getCommentListDtos().size() ; j++) {
                    System.out.println(hashMap.get(i).getCommentListDtos().get(j).getCommentNo());

                }

            }
        }

        List<CommentListDto> commentListDtos = new ArrayList<>();
        for (int i = 1; i < hashMap.size()+1; i++) {
            commentListDtos.add(hashMap.get(i));
        }

        return commentListDtos;
    }

//    public List<CommentListDto> saveList(CommentResponse) {
//        re
//    }

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
