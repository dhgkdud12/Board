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


//    private List<CommentResponse> getRootList (List<CommentResponse> commentList) {
//        // 계층별로
//    }

    public List<CommentListDto> selectCommentsByPostId(Integer bIdx) {
        List<CommentResponse> commentList = commentDao.selectCommentsByPostId(bIdx); // 게시물에 대한 모든 댓글 정보

        
        // ArrayList 계층별 생성, 계층은 2까지만
        List<CommentListDto> layer0List = new ArrayList<>();
        List<CommentListDto> layer1List = new ArrayList<>();
        List<CommentListDto> layer2List = new ArrayList<>();


        
        // commentList.size만큼 반복문, if layer 0, 1, 2에 따라
        for (int i = 0; i < commentList.size(); i++) {
            // dto 정보에 가져와서 만들고 list 생성
            CommentResponse commentInfo = commentList.get(i);
            List<CommentListDto> commentLayer = new ArrayList<>();
            if (commentInfo.getLayer() == 2) commentLayer = null;

            CommentListDto commentListDto =
                    CommentListDto.builder()
                            .commentNo(commentInfo.getCommentNo())
                            .boardNo(commentInfo.getBoardNo())
                            .content(commentInfo.getContent())
                            .userIdx(commentInfo.getUserIdx())
                            .userName(commentInfo.getUserName())
                            .date(commentInfo.getDate())
                            .parentId(commentInfo.getParentId())
                            .groupNo(commentInfo.getGroupNo())
                            .layer(commentInfo.getLayer())
                            .childCnt(commentInfo.getChildCnt())
                            .groupOrd(commentInfo.getGroupOrd())
                            .commentListDtos(commentLayer)
                            .build();

            Integer layer = commentInfo.getLayer();
            switch (layer) {
                case 0: // layer 0이면 0에 저장
                    layer0List.add(commentListDto);
                    break;
                case 1: // layer 1이면 1에 저장
                    layer1List.add(commentListDto);
                    break;
                case 2: // layer 2이면 2에 저장
                    layer2List.add(commentListDto);
                    break;

            }
        }

        layer1List = addCommentList(layer1List, layer2List);
        layer0List = addCommentList(layer0List, layer1List);
        
        return layer0List;
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

            if (commentDao.insertComment(comment) != 0) return "댓글 작성 완료";
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
