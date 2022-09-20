package spring.board.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.board.dto.UpdateBoardDto;
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
    public Board selectPostByPostId(Integer id) {
        String query = "SELECT * FROM board WHERE board_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcBoardDao.BoardRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 내가 쓴 게시물 가져오기
    public List<Board> selectPostByUserId(Integer id) {
        String query = "SELECT * FROM board WHERE user_idx = ?";
        try {
            return jdbcTemplate.query(query, new JdbcBoardDao.BoardRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 게시물 전체 가져오기
    public List<Board> selectPost() {
        String query = "SELECT * FROM board";
        try {
            return jdbcTemplate.query(query, new JdbcBoardDao.BoardRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public class BoardRowMapper implements RowMapper<Board> {
        @Override
        public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Board(
                    rs.getInt("board_no"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getInt("user_idx"),
                    rs.getTimestamp("create_date"),
                    rs.getTimestamp("update_date")
            );
        }
    }

    // 게시물 수정
    public void updatePost(UpdateBoardDto boardDto){
        String query = "UPDATE board SET title = ?, content = ?, update_date = ? where board_no = ? " ;
        jdbcTemplate.update(query, boardDto.getTitle(), boardDto.getContent(), boardDto.getUpdateTime(), boardDto.getBoardNo());
    }

    public void deletePost(Integer bIdx){
        String query = "DELETE FROM board WHERE board_no = ?" ;
        jdbcTemplate.update(query, bIdx);
    }
}
