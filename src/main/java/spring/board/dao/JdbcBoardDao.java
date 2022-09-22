package spring.board.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.board.dto.BoardResponse;
import spring.board.dto.BoardUpdateRequest;
import spring.board.entity.Board;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcBoardDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcBoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 게시물 등록
    public Integer insertPost(Board board){
        String query = "INSERT INTO board (title, content, user_idx, create_date, update_date) values (?, ?, ?, ?, ?)" ;
        jdbcTemplate.update(query, board.getTitle(), board.getContent(), board.getUserIdx(), board.getCreateDate(), board.getUpdateDate());
        return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
    }

    // 개별 게시물 가져오기
    public BoardResponse selectPostByPostId(Integer id) {
        String query = "SELECT b.board_no, b.title, b.content, b.user_idx, u.name, b.create_date, b.update_date FROM board b inner join user u on b.user_idx = u.idx WHERE b.board_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcBoardDao.BoardRespMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 개별 게시물 사용자 이름 포함 가져오기
    public BoardResponse selectPostNByPostId(Integer id) {
        String query = "SELECT b.board_no, b.title, b.content, b.user_idx, u.name, b.create_date, b.update_date FROM board b inner join user u on b.user_idx = u.idx WHERE b.board_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcBoardDao.BoardRespMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 내가 쓴 게시물 가져오기
    public List<BoardResponse> selectPostByUserId(Integer id) {
        String query = "SELECT b.board_no, b.title, b.content, b.user_idx, u.name, b.create_date, b.update_date FROM board b inner join user u on b.user_idx = u.idx WHERE b.user_idx = ?";
        try {
            return jdbcTemplate.query(query, new JdbcBoardDao.BoardRespMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 게시물 전체 가져오기
    public List<BoardResponse> selectPost(int page) {
        String query = "SELECT b.board_no, b.title, b.content, b.user_idx, u.name, b.create_date, b.update_date FROM board b inner join user u on b.user_idx = u.idx ORDER BY b.board_no ASC LIMIT 10 OFFSET ?;";
        try {
            return jdbcTemplate.query(query, new JdbcBoardDao.BoardRespMapper(),(page-1)*10);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public class BoardRespMapper implements RowMapper<BoardResponse> {
        @Override
        public BoardResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BoardResponse(
                    rs.getInt("board_no"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getInt("user_idx"),
                    rs.getString("name"),
                    rs.getTimestamp("create_date"),
                    rs.getTimestamp("update_date")
            );
        }
    }

    // 게시물 수정
    public void updatePost(BoardUpdateRequest boardDto){
        String query = "UPDATE board SET title = ?, content = ?, update_date = ? where board_no = ? " ;
        jdbcTemplate.update(query, boardDto.getTitle(), boardDto.getContent(), boardDto.getUpdateTime(), boardDto.getBoardNo());
    }

    public void deletePost(Integer bIdx){
        String query = "DELETE FROM board WHERE board_no = ?" ;
        jdbcTemplate.update(query, bIdx);
    }
}
