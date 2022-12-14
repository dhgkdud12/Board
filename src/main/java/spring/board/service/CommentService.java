package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.common.response.CommonResponse;
import spring.board.common.response.ResponseStatus;
import spring.board.common.response.exception.ErrorCode;
import spring.board.common.response.exception.TicketingException;
import spring.board.common.response.SuccessMessage;
import spring.board.dao.MyBatis.BoardMapper;
import spring.board.dao.MyBatis.CommentMapper;
import spring.board.domain.Comment;
import spring.board.dto.comment.CommentDto;
import spring.board.dto.comment.CommentRequest;
import spring.board.dto.comment.CommentResponse;
import spring.board.dto.user.UserSession;
import spring.board.util.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CommentService {
    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;
    private final UserService userService;
//    private final JdbcBoardDao boardDao;
//    private final JdbcCommentDao commentDao;

    public CommentService(BoardMapper boardMapper, CommentMapper commentMapper, UserService userService) {
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
//        for (int i = 0; i < commentList.size(); i++) { // ?????????
//            CommentResponse curComment = commentList.get(i);
//            Integer curParentId = curComment.getParentId();
////            if (parentId == 0 || curParentId == 0) continue;
//
////            System.out.println("parentId != 0??? ??? 0: "+ parentId + " " + curComment.getParentId() + " " + curComment.getCommentNo());
//
//            if (Objects.equals(curParentId, parentId) && (curComment.getLayer() == layer)) {
////                System.out.println("?????? id ?????? ??? 1: "+ parentId + " " + curParentId + " " + curComment.getCommentNo());
//                CommentDto curCommentDto = createCommentListDto(curComment);
//                commentDto.getCommentDtos().add(curCommentDto);
////                System.out.println(curCommentDto.getCommentNo() + " " + curCommentDto.getContent());
//                // ?????? ??? ????????????
//                // ?????? ????????? ??????
//                List<CommentDto> commentDtos = new ArrayList<>(); //?????? ???
//                for (int j = 0; j <commentList.size(); j++) {
//                    if (commentList.get(j).getParentId() == curCommentDto.getCommentNo()) {
//                        CommentDto commentDtoT = createCommentListDto(commentList.get(j)); // ??????1, ??????2 ??????
//                        commentDtos.add(commentDtoT); //
//                    }
//                }
//
//                for (int j = 0; j < commentDtos.size() ; j++) { // ??????1??? ??????1??? ??????1, ??????1??? ??????1??? ??????2
//                    commentDto.getCommentDtos().add(commentDtos.get(j));
//                    recursiveComment(commentList, layer+1, commentDtos.get(j).getCommentNo(), commentDto.getCommentDtos().get(j)); // ????????? ??? ??????
//                }
//
////                return recursiveComment(commentList, layer+1, curComment.getCommentNo(), curCommentDto);
//            } else {
////                System.out.println("?????? id ?????? ??? 2: "+ parentId + " " + curParentId + " " + curComment.getCommentNo());
//            }
//        }
//        return commentDto;
//    }

//    public List<CommentDto> selectCommentsByPostId(Integer bIdx) {
//        List<CommentResponse> commentList = commentDao.selectCommentsByPostId(bIdx); // ???????????? ?????? ?????? ?????? ??????
//        List<CommentDto> resultCommentList = new ArrayList<>(); // ???????????? ????????? ?????????
//
//        for (int i = 0; i < commentList.size(); i++) {
//            CommentResponse curComment = commentList.get(i);
//            if (curComment.getParentId() == 0) { // ?????? id??? 0?????? ??????????????? ??????
//                CommentDto commentDto = createCommentListDto(curComment); // ??????1, ??????2, ??????3, ??????4
//                List<CommentDto> commentDtos = new ArrayList<>(); //?????? ???
//                for (int j = 0; j <commentList.size() ; j++) {
//                    if (commentList.get(j).getParentId() == commentDto.getCommentNo()) {
//                        CommentDto commentDtoT = createCommentListDto(commentList.get(j)); // ??????1, ??????2 ??????
//                        commentDtos.add(commentDtoT); //
//                    }
//                }
//                for (int j = 0; j < commentDtos.size() ; j++) { // ??????1??? ??????1??? ??????1, ??????1??? ??????1??? ??????2
//                    commentDto.getCommentDtos().add(commentDtos.get(j));
//                    recursiveComment(commentList, 2, commentDtos.get(j).getCommentNo(), commentDto.getCommentDtos().get(j)); // ????????? ??? ??????
//                }
//
//                resultCommentList.add(commentDto);
//            }
//        }
//        return resultCommentList;
//    }

//    private List<CommentDto> recursiveComment (List<CommentResponse> groupList, Integer parentId, Integer layer) { // groupList, ?????? id, layer
//
//        // stream - lamda
////        List<CommentDto> childList = list.stream()
////                .filter(r-> Objects.equals(r.getLayer(), layer))
////                .filter(r-> Objects.equals(r.getParentId(), parentId))
////                .map(this::createCommentListDto)
////                .collect(Collectors.toList()); // ???????????? ??????
////        childList.forEach(r-> {
////            r.setCommentDtos(recursiveComment(list, r.getCommentNo(), layer+1));
////        });
//
//        //foreach
//        List<CommentDto> childList = new ArrayList<>();
//        for (CommentResponse r : groupList) { // ?????? ??????
//            if (Objects.equals(r.getLayer(), layer)  // layer+1 ?????? ????????? ????????????
//                    && Objects.equals(r.getParentId(), parentId)) { // parentId?????? ????????? comment_no??? ?????????
//                childList.add(createCommentListDto(r)); // list??? ??????
//            }
//        }
//        for (CommentDto r : childList) { // ???????????? ?????? ???????????? ?????? ????????? ????????????
//            r.setCommentDtos(recursiveComment(groupList, r.getCommentNo(), layer+1)); // groupList ?????????, CommentNo???, layer+1
//        }
//        return childList;
//    }

//    public List<CommentDto> selectCommentsByPostId(Integer bIdx) {
//        List<CommentResponse> commentList = commentMapper.selectCommentsByPostId(bIdx); // ?????? ?????? ??????
//        List<CommentDto> respList = new ArrayList<>(); // ???????????????
//        for (CommentResponse comment : commentList) {
//            if (comment.getLayer() == 0) { // ????????? 0??? ????????????(????????? ??????)???
//                int groupNo = comment.getGroupNo();
//                // stream
////                List<CommentResponse> groupList = commentList.stream()
////                        .filter(r->r.getGroupNo() == groupNo)
////                        .collect(Collectors.toList());
//
//                //foreach
//                List<CommentResponse> groupList = new ArrayList<>();
//                for (CommentResponse r : commentList) {
//                    if (r.getGroupNo() == groupNo) { // group_no??? ?????? (?????? 1, ?????? 2??? ?????? ??????) ????????? list??? ??? ?????? ?????? ?????? ??????
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
                CommentDto rootComment = createCommentListDto(comment); // ????????? ??????

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
//                CommentDto rootComment = createCommentListDto(comment); // ????????? ??????
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

    public CommonResponse post(Integer bIdx, CommentRequest commentRequest) throws Exception {
//        HttpSession session = request.getSession();
//        UserSession userSession = (UserSession) session.getAttribute("USER");
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");

        if (boardMapper.selectPostByPostId(bIdx) == null) {
            throw new TicketingException(ErrorCode.INVALID_BOARD);
        }
        else if (userSession == null) {
            throw new TicketingException(ErrorCode.INVALID_LOGIN);
        } else {
            Comment comment = null;
            if (commentRequest.getParentId() == null) {
                comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), null, null, 0, 0, 0);
                commentMapper.insertRootComment(comment);

            } else {
                if (commentMapper.selectCommentByCommentId(commentRequest.getParentId()) == null) {
                    throw new TicketingException(ErrorCode.INVALID_COMMENT);
                }
                // ?????? ????????? ?????? ???????????? ????????? ?????? ??????
                comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), commentRequest.getParentId(), null, 0, 0, 0);
                commentMapper.insertComment(comment);
                commentMapper.updateParentChildCnt(comment.getParentId());

            }
//            comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), commentRequest.getParentId(), null, 0, 0, 0);
//            jdbcCommentDao.insertComment(comment);

            return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "?????? " + SuccessMessage.SUCCESS_CREATE.getMessage(), null);
        }
    }

    // ?????? ????????? ????????? ?????????????????? ??????
    public CommonResponse delete(Integer cIdx) throws Exception {
//        UserSession userSession = userService.getLoginUserInfo(request);
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");
        Integer c_uidx = commentMapper.selectCommentByCommentId(cIdx).getUserIdx();

        if (c_uidx == null) throw new TicketingException(ErrorCode.INVALID_COMMENT);
        else {
            if (userSession == null) {
                throw new TicketingException(ErrorCode.INVALID_LOGIN);
            }
            else {
                if (!c_uidx.equals(userSession.getIdx())) {
                    throw new TicketingException(ErrorCode.INVALID_USER);
                } else {
                    commentMapper.deleteComment(cIdx);
                    return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "?????? " + SuccessMessage.SUCCESS_DELETE.getMessage(), null);
                }
            }
        }
    }

    public List<CommentResponse> selectCommentsByUserId() {
//        HttpSession session = request.getSession();
//        UserSession userSession = (UserSession) session.getAttribute("USER");
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");
        return commentMapper.selectCommentsByUserId(userSession.getIdx());
    }

    public CommentResponse selectCommentByUserId(HttpServletRequest request, Integer cIdx) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("USER");
        return commentMapper.selectCommentByUserId(userSession.getIdx(), cIdx);
    }
}
