package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import spring.board.dto.BoardResponse;
import spring.board.dto.BoardUpdateRequest;
import spring.board.entity.Board;
import spring.board.entity.Paging;

import java.util.List;

@Mapper
public interface BoardMapper {
    public Integer insertPost(Board board);
    public BoardResponse selectPostByPostId(Integer id);
    public BoardResponse selectPostNByPostId(Integer id);
    public List<BoardResponse> selectPostByUserId(@Param("paging") Paging paging, @Param("id") Integer id);
    public Integer getTotalCnt();
    public List<BoardResponse> selectPost(Paging paging);
    public BoardResponse searchPosts(String q);
    public void updatePost(BoardUpdateRequest boardDto);
    public void deletePost(Integer bIdx);
}
