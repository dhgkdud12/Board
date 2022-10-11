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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private CommentDto createCommentListDto(CommentResponse commentResponse) {
        List<CommentDto> commentLayer = new ArrayList<>();
        CommentDto curCommentDto =
                CommentDto.builder()
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
                        .commentDtos(commentLayer)
                        .build();
        return curCommentDto;
    }

//    private CommentDto recursiveComment (List<CommentResponse> commentList, Integer layer, Integer parentId, CommentDto commentDto) {
//
//        for (int i = 0; i < commentList.size(); i++) { // 자식수
//            CommentResponse curComment = commentList.get(i);
//            Integer curParentId = curComment.getParentId();
////            if (parentId == 0 || curParentId == 0) continue;
//
////            System.out.println("parentId != 0일 때 0: "+ parentId + " " + curComment.getParentId() + " " + curComment.getCommentNo());
//
//            if (Objects.equals(curParentId, parentId) && (curComment.getLayer() == layer)) {
////                System.out.println("부모 id 같을 때 1: "+ parentId + " " + curParentId + " " + curComment.getCommentNo());
//                CommentDto curCommentDto = createCommentListDto(curComment);
//                commentDto.getCommentDtos().add(curCommentDto);
////                System.out.println(curCommentDto.getCommentNo() + " " + curCommentDto.getContent());
//                // 자식 수 가져와서
//                // 자식 수만큼 반환
//                List<CommentDto> commentDtos = new ArrayList<>(); //자식 수
//                for (int j = 0; j <commentList.size(); j++) {
//                    if (commentList.get(j).getParentId() == curCommentDto.getCommentNo()) {
//                        CommentDto commentDtoT = createCommentListDto(commentList.get(j)); // 댓글1, 댓글2 하위
//                        commentDtos.add(commentDtoT); //
//                    }
//                }
//
////                for (int j = 0; j < commentDtos.size(); j++) {
////                    System.out.println(commentDtos.get(j).getCommentNo() + " " + commentDtos.get(j).getContent());
////                }
//
//                for (int j = 0; j < commentDtos.size() ; j++) { // 댓글1의 댓글1의 댓글1, 댓글1의 댓글1의 댓글2
//                    commentDto.getCommentDtos().add(commentDtos.get(j));
//                    recursiveComment(commentList, layer+1, commentDtos.get(j).getCommentNo(), commentDto.getCommentDtos().get(j)); // 마지막 거 반환
//                }
//
////                return recursiveComment(commentList, layer+1, curComment.getCommentNo(), curCommentDto);
//            } else {
////                System.out.println("부모 id 다를 때 2: "+ parentId + " " + curParentId + " " + curComment.getCommentNo());
//            }
//        }
//        return commentDto;
//    }

//    public List<CommentDto> selectCommentsByPostId(Integer bIdx) {
//        List<CommentResponse> commentList = commentDao.selectCommentsByPostId(bIdx); // 게시물에 대한 모든 댓글 정보
//        List<CommentDto> resultCommentList = new ArrayList<>(); // 계층형된 댓글의 리스트
//
//        for (int i = 0; i < commentList.size(); i++) {
//            CommentResponse curComment = commentList.get(i);
//            if (curComment.getParentId() == 0) { // 부모 id가 0이면 댓글리스트 생성
//                CommentDto commentDto = createCommentListDto(curComment); // 댓글1, 댓글2, 댓글3, 댓글4
//                List<CommentDto> commentDtos = new ArrayList<>(); //자식 수
//                for (int j = 0; j <commentList.size() ; j++) {
//                    if (commentList.get(j).getParentId() == commentDto.getCommentNo()) {
//                        CommentDto commentDtoT = createCommentListDto(commentList.get(j)); // 댓글1, 댓글2 하위
//                        commentDtos.add(commentDtoT); //
//                    }
//                }
//                for (int j = 0; j < commentDtos.size() ; j++) { // 댓글1의 댓글1의 댓글1, 댓글1의 댓글1의 댓글2
//                    commentDto.getCommentDtos().add(commentDtos.get(j));
//                    recursiveComment(commentList, 2, commentDtos.get(j).getCommentNo(), commentDto.getCommentDtos().get(j)); // 마지막 거 반환
//                }
//
//
//                resultCommentList.add(commentDto);
//
//            }
//        }
//
//        return resultCommentList;
//    }

    private List<CommentDto> recursiveComment (List<CommentResponse> list, Integer parentId, Integer layer) {
        // stream - lamda
//        List<CommentDto> childList = list.stream()
//                .filter(r-> Objects.equals(r.getLayer(), layer))
//                .filter(r-> Objects.equals(r.getParentId(), parentId))
//                .map(this::createCommentListDto)
//                .collect(Collectors.toList()); // 리스트로 변환
//        childList.forEach(r-> {
//            r.setCommentDtos(recursiveComment(list, r.getCommentNo(), layer+1));
//        });

        //foreach
        List<CommentDto> childList = new ArrayList<>();
        for (CommentResponse r : list) {
            if (Objects.equals(r.getLayer(), layer) && Objects.equals(r.getParentId(), parentId)) {
                childList.add(createCommentListDto(r));
            }
        }
        for (CommentDto r : childList) {
            r.setCommentDtos(recursiveComment(list, r.getCommentNo(), layer+1));
        }
        return childList;
    }


        public List<CommentDto> selectCommentsByPostId(Integer bIdx) {
        List<CommentResponse> commentList = commentDao.selectCommentsByPostId(bIdx); // 전체 댓글 조회
        List<CommentDto> respList = new ArrayList<>(); // 결과리스트 
        for (CommentResponse comment : commentList) {
            if (comment.getLayer() == 0) {
                int groupNo = comment.getGroupNo();
                // stream
//                List<CommentResponse> groupList = commentList.stream()
//                        .filter(r->r.getGroupNo() == groupNo)
//                        .collect(Collectors.toList());

                //foreach
                List<CommentResponse> groupList = new ArrayList<>();
                for (CommentResponse r : commentList) {
                    if (r.getGroupNo() == groupNo) {
                        groupList.add(r);
                    }
                }
                CommentDto rootComment = createCommentListDto(comment);
                rootComment.setCommentDtos(recursiveComment(groupList, rootComment.getCommentNo(), rootComment.getLayer() + 1));
                respList.add(rootComment);
            }
        }
        return respList;
    }

    private List<CommentDto> addCommentList(List<CommentDto> parent, List<CommentDto> child) {
        for (int i = 0; i < parent.size(); i++) {
            Integer commentId = parent.get(i).getCommentNo();
            for (int j = 0; j < child.size(); j++) {
                Integer parentId = child.get(j).getParentId();
                if (commentId == parentId) {
                    parent.get(i).getCommentDtos().add(child.get(j));
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
            }
            comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), commentRequest.getParentId(), null, 0, 0, 0);
            commentDao.insertComment(comment);

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
