package spring.board.dao.JdbcTemplate;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.board.dto.CommentResponse;
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
//        Integer groupNo = comment.getGroupNo();
        Integer groupOrd = comment.getGroupOrd();

        if (comment.getParentId() == null) {
            Integer groupNo = jdbcTemplate.queryForObject("select IFNULL(max(group_no),0)+1 from comment where parent_id IS null;", Integer.class);
            comment.setGroupNo(groupNo);
        } else {
            String query = "SELECT c.comment_no, c.board_no, c.content, c.user_idx, u.name, c.date, c.parent_id, c.group_no, c.layer, c.child_cnt, c.group_ord from comment c inner join user u on c.user_idx = u.idx where comment_no = ?";
            CommentResponse parentCo = jdbcTemplate.queryForObject(query, new JdbcCommentDao.CommentRowMapper(), comment.getParentId());
            comment.setGroupNo(parentCo.getGroupNo());

            groupOrd = jdbcTemplate.queryForObject("select  IFNULL(max(group_ord), 0)+1 from comment where parent_id = ?;", Integer.class, comment.getParentId());
//            comment.setGroupOrd(groupOrd);

            comment.setLayer(parentCo.getLayer()+1);
            comment.setGroupOrd(parentCo.getGroupOrd());

            String prQuery = "UPDATE comment SET child_cnt = child_cnt + 1 WHERE comment_no = ?";
            jdbcTemplate.update(prQuery, comment.getParentId());
        }

        String query = "INSERT INTO comment (board_no, content, user_idx, date, parent_id, group_no, layer, child_cnt, group_ord ) values (?, ?, ?, ?, ?, ?, ?, ?, ?)" ;
        jdbcTemplate.update(query, comment.getBoardNo(), comment.getContent(), comment.getUserIdx(), comment.getDate(), comment.getParentId(), comment.getGroupNo(), comment.getLayer(), comment.getChildCnt(), groupOrd);
    }

    // 게시물별 댓글 가져오기
    public List<CommentResponse> selectCommentsByPostId(Integer bIdx) {
        String query = "select c.comment_no, c.board_no, c.content, c.user_idx, u.name, c.date, c.parent_id, c.group_no, layer, child_cnt, group_ord from comment c inner join user u on c.user_idx = u.idx where c.board_no = ? order by comment_no";
        try {
            return jdbcTemplate.query(query, new JdbcCommentDao.CommentRowMapper(), bIdx);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 게시물별 댓글 인덱스 새로
    public CommentResponse selectCommentByCommentId(Integer id) {
        String query = "SELECT c.comment_no, c.board_no, c.content, c.user_idx, u.name, c.date, c.parent_id, c.group_no, layer, child_cnt, group_ord FROM comment c INNER JOIN user u on c.user_idx = u.idx WHERE comment_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcCommentDao.CommentRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // x
    public CommentResponse selectCommentByUserId(Integer id, Integer uIdx) {
        String query = "SELECT c.comment_no, c.board_no, c.content, c.user_idx, u.name, c.date, c.parent_id, c.group_no, layer, child_cnt, group_ord FROM comment c INNER JOIN user u on c.user_idx = u.idx WHERE user_idx = ? AND comment_no = ?";
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

    public List<CommentResponse> selectCommentsByUserId(Integer uIdx) {
        String query = "select c.comment_no, c.board_no, c.content, c.user_idx, u.name, c.date, c.parent_id, c.group_no, layer, child_cnt, group_ord from comment c inner join user u on c.user_idx = u.idx where c.user_idx = ? order by comment_no";
        return jdbcTemplate.query(query, new JdbcCommentDao.CommentRowMapper(), uIdx);
    }

    public class CommentRowMapper implements RowMapper<CommentResponse> {
        @Override
        public CommentResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CommentResponse(
                    rs.getInt("comment_no"),
                    rs.getInt("board_no"),
                    rs.getString("content"),
                    rs.getInt("user_idx"),
                    rs.getString("name"),
                    rs.getTimestamp("date"),
                    rs.getInt("parent_id"),
                    rs.getInt("group_no"),
                    rs.getInt("layer"),
                    rs.getInt("child_cnt"),
                    rs.getInt("group_ord")
            );
        }
    }
}

