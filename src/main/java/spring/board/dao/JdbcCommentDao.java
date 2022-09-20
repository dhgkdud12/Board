package spring.board.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.board.entity.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcCommentDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCommentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertComment(Comment comment){
        String query = "INSERT INTO comment (board_no, content, user_idx, date) values (?, ?, ?, ?)" ;
        jdbcTemplate.update(query, comment.getBoardNo(), comment.getContent(), comment.getUserIdx(), comment.getDate());
    }

    // 게시물별 댓글 가져오기
    public List<Comment> selectCommentsByPostId(Integer bIdx) {
        String query = "SELECT * FROM comment where board_no = ?";
        try {
            return jdbcTemplate.query(query, new JdbcCommentDao.CommentRowMapper(), bIdx);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Comment selectCommentByCommentId(Integer id) {
        String query = "SELECT * FROM comment WHERE comment_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcCommentDao.CommentRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Comment selectCommentByUserId(Integer id, Integer uIdx) {
        String query = "SELECT * FROM comment WHERE user_idx = ? AND comment_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcCommentDao.CommentRowMapper(), id, uIdx);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void deleteComment(Integer cIdx) {
        String query = "DELETE FROM comment WHERE comment_no = ?" ;
        jdbcTemplate.update(query, cIdx);
    }

    public List<Comment> selectCommentsByUserId(Integer uIdx) {
        String query = "SELECT * FROM comment WHERE user_idx = ?";
        return jdbcTemplate.query(query, new JdbcCommentDao.CommentRowMapper(), uIdx);
    }

    public class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Comment(
                    rs.getInt("comment_no"),
                    rs.getInt("board_no"),
                    rs.getString("content"),
                    rs.getInt("user_idx"),
                    rs.getTimestamp("date")
            );
        }
    }
}

