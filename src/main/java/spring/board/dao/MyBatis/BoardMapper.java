package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.dto.board.BoardResponse;
import spring.board.dto.board.BoardSearchRequest;
import spring.board.dto.board.BoardUpdateRequest;
import spring.board.domain.Board;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    Integer insertPost(Board board);
    BoardResponse selectPostByPostId(Integer id);
    List<BoardResponse> selectPostsByUserId(Map map);
    Integer getTotalCnt();
    List<BoardResponse> selectPost(BoardSearchRequest search);
    BoardResponse searchPosts(String q);
    void updatePost(BoardUpdateRequest boardDto);
    void deletePost(Integer bIdx);
}
