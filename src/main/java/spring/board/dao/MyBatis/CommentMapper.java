package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.dto.CommentResponse;
import spring.board.entity.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    public void insertComment(Comment comment);
    public Integer selectGroupNoNotParent(Integer parentId);
    public Integer selectGroupNo(Integer parentId);

    public List<CommentResponse> selectCommentsByPostId(Integer bIdx);

    public CommentResponse selectCommentByCommentId(Integer id);

    public CommentResponse selectCommentByUserId(Integer id, Integer uIdx);

    public void deleteComment(Integer cIdx);

    public List<CommentResponse> selectCommentsByUserId(Integer uIdx);
}
