package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.common.ErrorCode;
import spring.board.common.TicketingException;
import spring.board.dao.JdbcTemplate.JdbcBoardDao;
import spring.board.dao.JdbcTemplate.JdbcCommentDao;
import spring.board.dao.MyBatis.BoardMapper;
import spring.board.dao.MyBatis.CommentMapper;
import spring.board.domain.Comment;
import spring.board.dto.comment.CommentDto;
import spring.board.dto.comment.CommentRequest;
import spring.board.dto.comment.CommentResponse;
import spring.board.dto.user.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CommentService {
    private final UserService userService;
    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;
//    private final JdbcBoardDao boardDao;
//    private final JdbcCommentDao commentDao;

    public CommentService(UserService userService, JdbcBoardDao boardDao, BoardMapper boardMapper, JdbcCommentDao commentDao, CommentMapper commentMapper) {
        this.userService = userService;
        this.boardMapper = boardMapper;
        this.commentMapper = commentMapper;
//        this.boardDao = boardDao;
//        this.commentDao = commentDao;
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
//                resultCommentList.add(commentDto);
//            }
//        }
//        return resultCommentList;
//    }

//    private List<CommentDto> recursiveComment (List<CommentResponse> groupList, Integer parentId, Integer layer) { // groupList, 부모 id, layer
//
//        // stream - lamda
////        List<CommentDto> childList = list.stream()
////                .filter(r-> Objects.equals(r.getLayer(), layer))
////                .filter(r-> Objects.equals(r.getParentId(), parentId))
////                .map(this::createCommentListDto)
////                .collect(Collectors.toList()); // 리스트로 변환
////        childList.forEach(r-> {
////            r.setCommentDtos(recursiveComment(list, r.getCommentNo(), layer+1));
////        });
//
//        //foreach
//        List<CommentDto> childList = new ArrayList<>();
//        for (CommentResponse r : groupList) { // 자식 후보
//            if (Objects.equals(r.getLayer(), layer)  // layer+1 값과 같다면 자식계층
//                    && Objects.equals(r.getParentId(), parentId)) { // parentId값이 부모의 comment_no와 같다면
//                childList.add(createCommentListDto(r)); // list에 추가
//            }
//        }
//        for (CommentDto r : childList) { // 자식으로 묶은 댓글들의 하위 댓글을 찾기위해
//            r.setCommentDtos(recursiveComment(groupList, r.getCommentNo(), layer+1)); // groupList 그대로, CommentNo값, layer+1
//        }
//        return childList;
//    }

//    public List<CommentDto> selectCommentsByPostId(Integer bIdx) {
//        List<CommentResponse> commentList = commentMapper.selectCommentsByPostId(bIdx); // 전체 댓글 조회
//        List<CommentDto> respList = new ArrayList<>(); // 결과리스트
//        for (CommentResponse comment : commentList) {
//            if (comment.getLayer() == 0) { // 계층이 0인 루트댓글(최상위 댓글)만
//                int groupNo = comment.getGroupNo();
//                // stream
////                List<CommentResponse> groupList = commentList.stream()
////                        .filter(r->r.getGroupNo() == groupNo)
////                        .collect(Collectors.toList());
//
//                //foreach
//                List<CommentResponse> groupList = new ArrayList<>();
//                for (CommentResponse r : commentList) {
//                    if (r.getGroupNo() == groupNo) { // group_no가 같은 (댓글 1, 댓글 2에 대한 것들) 것들을 list에 다 담아 그룹 별로 분리
//                        groupList.add(r);
//                    }
//                }
//                CommentDto rootComment = createCommentListDto(comment);
//                rootComment.setCommentDtos(recursiveComment(groupList, rootComment.getCommentNo(), rootComment.getLayer() + 1));
//                respList.add(rootComment);
//            }
//        }
//        return respList;
//    }


//    private List<CommentDto> addCommentList(List<CommentDto> parent, List<CommentDto> child) {
//        for (int i = 0; i < parent.size(); i++) {
//            Integer commentId = parent.get(i).getCommentNo();
//            for (int j = 0; j < child.size(); j++) {
//                Integer parentId = child.get(j).getParentId();
//                if (commentId == parentId) {
//                    parent.get(i).getCommentDtos().add(child.get(j));
//                }
//            }
//        }
//        return parent;
//    }

//    private List<CommentDto> recursiveComment (List<CommentDto> groupList, Integer parentId, Integer layer, Integer childCnt) {
//
//        List<CommentDto> childList = new ArrayList<>();
//        for (CommentDto r : groupList) {
//            if (Objects.equals(r.getLayer(), layer) && Objects.equals(r.getParentId(), parentId)) {
//                childList.add(r);
//            }
//        }
//        for (CommentDto r : childList) {
//            r.setCommentDtos(recursiveComment(groupList, r.getCommentNo(), layer+1, r.getChildCnt()));
//        }
//        return childList;
//    }

    public List<CommentDto> selectCommentsByPostId(Integer bIdx) {
        List<CommentResponse> orderedComments = commentMapper.selectRecursiveComments(bIdx);
        List<CommentDto> respList = new ArrayList<>();

        for (CommentResponse comment : orderedComments) {
            if (comment.getParentId() == 0) {
                List<CommentDto> groupList = new ArrayList<>();
                CommentDto rootComment = createCommentListDto(comment); // 최상위 댓글

                for(CommentResponse c : orderedComments) {
                    if (comment.getGroupNo() == c.getGroupNo()) {
                        groupList.add(createCommentListDto(c));
                    }
                }

                rootComment.setCommentDtos(recursiveComment(groupList, rootComment.getCommentNo(), rootComment.getLayer()+1, rootComment.getChildCnt()));
                respList.add(rootComment);
            }
        }
        return respList;
    }

//    public List<CommentDto> selectCommentsByPostId(Integer bIdx) {
//        List<CommentResponse> orderedComments = commentMapper.selectRecursiveComments(bIdx);
//
//        List<CommentDto> respList = new ArrayList<>();
//
//        for (CommentResponse comment : orderedComments) {
//            if (comment.getParentId() == 0) {
//                List<CommentDto> groupList = new ArrayList<>();
//                CommentDto rootComment = createCommentListDto(comment); // 최상위 댓글
//
//                for(CommentResponse c : orderedComments) {
//                    if (comment.getGroupNo() == c.getGroupNo()) {
//                        groupList.add(createCommentListDto(c));
//                    }
//                }
//
//                List<CommentDto> commentDtos = new ArrayList<>();
//                List<CommentDto> commentDtos2 = new ArrayList<>();
//
//                commentDtos.add(groupList.get(1));
//
//                if (groupList.get(1).getChildCnt() > 0) {
//
//                    for (int i = 0; i < groupList.get(1).getChildCnt(); i++) {
//                        commentDtos2.add(groupList.get(2 + i));
//                    }
//
//                }
//
//                if (groupList.get(0).getChildCnt() > 0) {
//                    for (int i = 0; i < groupList.get(0).getChildCnt() - 1 ; i++) {
//                        commentDtos.add(groupList.get(5+i));
//                    }
//                }
//
//                if (groupList.size()>1) {
//                    groupList.get(1).setCommentDtos(commentDtos2);
//                }
//                groupList.get(0).setCommentDtos(commentDtos);
//
//                respList.add(groupList.get(0));
//            }
//
//        }
//
//        return respList;
//    }

    private List<CommentDto> recursiveComment (List<CommentDto> groupList, Integer parentId, Integer layer, Integer childCnt) {

        List<CommentDto> childList = new ArrayList<>();
        for (CommentDto r : groupList) {
            if (Objects.equals(r.getLayer(), layer) && Objects.equals(r.getParentId(), parentId)) {
                childList.add(r);
            }
        }
        for (CommentDto r : childList) {
            r.setCommentDtos(recursiveComment(groupList, r.getCommentNo(), layer+1, r.getChildCnt()));
        }
        return childList;
    }

    public String post(Integer bIdx, CommentRequest commentRequest, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");

        if (boardMapper.selectPostByPostId(bIdx) == null) {
            throw new TicketingException(ErrorCode.INVALID_BOARD);
        }
        else if (userSession != null) {
            Comment comment = null;
            if (commentRequest.getParentId() == null) {
                comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), null, null, 0, 0, 0);
                commentMapper.insertRootComment(comment);

            } else {
                if (commentMapper.selectCommentByCommentId(commentRequest.getParentId()) == null) {
                    throw new TicketingException(ErrorCode.INVALID_COMMENT);
                }
                // 부모 댓글이 해당 게시글의 댓글이 아닐 경우
                comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), commentRequest.getParentId(), null, 0, 0, 0);
                commentMapper.insertComment(comment);
                commentMapper.updateParentChildCnt(comment.getParentId());

            }
//            comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), commentRequest.getParentId(), null, 0, 0, 0);
//            jdbcCommentDao.insertComment(comment);

            return "댓글 작성 완료";

        } else {
            throw new TicketingException(ErrorCode.INVALID_LOGIN);
        }
    }

    // 댓글 삭제시 삭제된 댓글입니다로 변경
    public String delete(Integer cIdx, HttpServletRequest request) { // 글이 존재하지 않을 경우
        UserSession userSession = userService.getLoginUserInfo(request);
        Integer c_uidx = commentMapper.selectCommentByCommentId(cIdx).getUserIdx();

        if (c_uidx == null) throw new TicketingException(ErrorCode.INVALID_COMMENT);
        else if (userSession != null ) {
            if (c_uidx.equals(userSession.getIdx())) {
                commentMapper.deleteComment(cIdx);
                return "댓글 삭제 완료";
            } else {
                throw new TicketingException(ErrorCode.INVALID_USER);
            }
        }
        return "댓글 삭제 실패";
    }

    public List<CommentResponse> selectCommentsByUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");
        return commentMapper.selectCommentsByUserId(userSession.getIdx());
    }

    public CommentResponse selectCommentByUserId(HttpServletRequest request, Integer cIdx) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");
        return commentMapper.selectCommentByUserId(userSession.getIdx(), cIdx);
    }
}
