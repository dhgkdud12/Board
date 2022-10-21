package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.dto.board.BoardResponse;
import spring.board.dto.board.BoardSearchRequest;
import spring.board.dto.board.BoardUpdateRequest;
import spring.board.domain.Board;
import spring.board.domain.Paging;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    public Integer insertPost(Board board);
    public BoardResponse selectPostByPostId(Integer id);
    public List<BoardResponse> selectPostsByUserId(Map map);
    public Integer getTotalCnt();
    public List<BoardResponse> selectPost(BoardSearchRequest search);
    public BoardResponse searchPosts(String q);
    public void updatePost(BoardUpdateRequest boardDto);
    public void deletePost(Integer bIdx);
}
