package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.dto.BoardResponse;
import spring.board.dto.BoardUpdateRequest;
import spring.board.entity.Board;
import spring.board.entity.Paging;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    public Integer insertPost(Board board);
    public BoardResponse selectPostByPostId(Integer id);
    public List<BoardResponse> selectPostsByUserId(Map map);
    public Integer getTotalCnt();
    public List<BoardResponse> selectPost(Paging paging);
    public BoardResponse searchPosts(String q);
    public void updatePost(BoardUpdateRequest boardDto);
    public void deletePost(Integer bIdx);
}
