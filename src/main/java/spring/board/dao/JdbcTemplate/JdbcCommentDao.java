package spring.board.dao.JdbcTemplate;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.board.dto.comment.CommentResponse;
import spring.board.domain.Comment;

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

        // parentId, groupNo
        if (comment.getParentId() == null) { // 루트 댓글일 때
            Integer groupNo = jdbcTemplate.queryForObject(
                    "SELECT IFNULL(MAX(group_no),0)+1 " +
                    "FROM comment " +
                    "WHERE parent_id IS null;",
                    Integer.class);
            comment.setGroupNo(groupNo);
        } else { // 대댓글일 때, 부모 댓글이 존재할 때
            // parentId, groupNo, groupOrd, layer
            String query = "SELECT comment_no, board_no, content, user_idx, date, parent_id, group_no, layer, child_cnt, group_ord " +
                            "FROM comment " +
                            "WHERE comment_no = ?";
            Comment parentCo = jdbcTemplate.queryForObject(query, new JdbcCommentDao.CommentRowMapper(), comment.getParentId());
            comment.setGroupNo(parentCo.getGroupNo()); // 부모 댓글에 대한 group_no
            groupOrd = jdbcTemplate.queryForObject(
                    "SELECT  IFNULL(max(group_ord), 0)+1 " +
                    "FROM comment " +
                    "WHERE parent_id = ?;",
                    Integer.class, comment.getParentId());
            comment.setGroupOrd(groupOrd); // group 순서 + 1
            comment.setLayer(parentCo.getLayer()+1); // layer + 1

            // 부모 댓글 자식 수 +1
            String prQuery = "UPDATE comment SET child_cnt = child_cnt + 1 " +
                    "WHERE comment_no = ?";
            jdbcTemplate.update(prQuery, comment.getParentId());
        }

        // 댓글 등록
        String query = "INSERT INTO comment (board_no, content, user_idx, date, parent_id, group_no, layer, child_cnt, group_ord ) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)" ;
        jdbcTemplate.update(query, comment.getBoardNo(), comment.getContent(), comment.getUserIdx(), comment.getDate(), comment.getParentId(), comment.getGroupNo(), comment.getLayer(), comment.getChildCnt(), groupOrd);
    }

    // 게시물별 댓글 가져오기
    public List<CommentResponse> selectCommentsByPostId(Integer bIdx) {
        String query = "SELECT" +
                " c.comment_no, c.board_no, c.content, c.user_idx, u.name, c.date, c.parent_id, c.group_no, layer, child_cnt, group_ord " +
                "FROM comment c " +
                "INNER JOIN user u " +
                "ON c.user_idx = u.idx " +
                "WHERE c.board_no = ? " +
                "ORDER BY comment_no";
        try {
            return jdbcTemplate.query(query, new JdbcCommentDao.CommentAndNameRowMapper(), bIdx);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 게시물별 댓글 인덱스 새로
    public CommentResponse selectCommentByCommentId(Integer id) {
        String query = "SELECT c.comment_no, c.board_no, c.content, c.user_idx, u.name, c.date, c.parent_id, c.group_no, layer, child_cnt, group_ord " +
                "FROM comment c " +
                "INNER JOIN user u " +
                "ON c.user_idx = u.idx " +
                "WHERE comment_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcCommentDao.CommentAndNameRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // x
    public CommentResponse selectCommentByUserId(Integer id, Integer uIdx) {
        String query = "SELECT c.comment_no, c.board_no, c.content, c.user_idx, u.name, c.date, c.parent_id, c.group_no, layer, child_cnt, group_ord " +
                "FROM comment c " +
                "INNER JOIN user u " +
                "ON c.user_idx = u.idx " +
                "WHERE user_idx = ? " +
                "AND comment_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcCommentDao.CommentAndNameRowMapper(), id, uIdx);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void deleteComment(Integer cIdx) {
        String query = "DELETE FROM comment WHERE comment_no = ?" ;
        jdbcTemplate.update(query, cIdx);
    }

    public List<CommentResponse> selectCommentsByUserId(Integer uIdx) {
        String query = "SELECT c.comment_no, c.board_no, c.content, c.user_idx, u.name, c.date, c.parent_id, c.group_no, layer, child_cnt, group_ord " +
                "FROM comment c " +
                "INNER JOIN user u " +
                "ON c.user_idx = u.idx " +
                "WHERE c.user_idx = ? " +
                "ORDER BY comment_no";
        return jdbcTemplate.query(query, new JdbcCommentDao.CommentAndNameRowMapper(), uIdx);
    }


    public class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Comment(
                    rs.getInt("comment_no"),
                    rs.getInt("board_no"),
                    rs.getString("content"),
                    rs.getInt("user_idx"),
                    rs.getTimestamp("date"),
                    rs.getInt("parent_id"),
                    rs.getInt("group_no"),
                    rs.getInt("layer"),
                    rs.getInt("child_cnt"),
                    rs.getInt("group_ord")
            );
        }
    }

    public class CommentAndNameRowMapper implements RowMapper<CommentResponse> {
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

