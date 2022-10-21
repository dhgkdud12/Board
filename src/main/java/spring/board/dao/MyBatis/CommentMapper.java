package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.dto.comment.CommentResponse;
import spring.board.domain.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    public void insertRootComment(Comment comment);
    public Integer insertComment(Comment comment);

    public List<CommentResponse> selectCommentsByPostId(Integer bIdx);

    public CommentResponse selectCommentByCommentId(Integer id);

    public CommentResponse selectCommentByUserId(Integer id, Integer uIdx);

    public void deleteComment(Integer cIdx);

    public List<CommentResponse> selectCommentsByUserId(Integer uIdx);
    public List<CommentResponse> selectRecursiveComments(Integer uIdx);


    public void updateParentChildCnt(Integer parentId);
}
