package spring.board.dao.JdbcTemplate;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.board.dto.BoardResponse;
import spring.board.dto.BoardUpdateRequest;
import spring.board.entity.Board;
import spring.board.entity.Paging;

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
        String query = "INSERT INTO board (title, content, user_idx, create_date, update_date)" +
                "VALUES (?, ?, ?, ?, ?)" ;
        jdbcTemplate.update(query, board.getTitle(), board.getContent(), board.getUserIdx(), board.getCreateDate(), board.getUpdateDate());
        return jdbcTemplate.queryForObject("SELECT last_insert_id()", Integer.class);
    }

    // 개별 게시물 가져오기
    public BoardResponse selectPostByPostId(Integer id) {
        String query = "SELECT b.board_no, b.title, b.content, b.user_idx, u.name, b.create_date, b.update_date " +
                "FROM board b " +
                "INNER JOIN user u " +
                "ON b.user_idx = u.idx " +
                "WHERE b.board_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcBoardDao.BoardRespMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 개별 게시물 사용자 이름 포함 가져오기
    public BoardResponse selectPostNByPostId(Integer id) {
        String query = "SELECT b.board_no, b.title, b.content, b.user_idx, u.name, b.create_date, b.update_date " +
                "FROM board b " +
                "INNER JOIN user u " +
                "ON b.user_idx = u.idx " +
                "WHERE b.board_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcBoardDao.BoardRespMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 내가 쓴 게시물 가져오기
    public List<BoardResponse> selectPostByUserId(Paging paging, Integer id) {
//        String query = "SELECT b.board_no, b.title, b.content, b.user_idx, u.name, b.create_date, b.update_date FROM board b inner join user u on b.user_idx = u.idx WHERE b.user_idx = ?";
        String query = "SELECT board_no, title, content, user_idx, name, create_date, update_date" +
                "FROM" +
                "(SELECT ROW_NUMBER() OVER ( ORDER BY board_no ASC) AS row_num, board_no, title, content, user_idx, create_date, update_date" +
                "FROM board WHERE user_idx = ?) b " +
                "INNER JOIN user u " +
                "ON b.user_idx = u.idx " +
                "WHERE row_num BETWEEN ? AND ? " +
                "ORDER BY row_num";
        try {
            return jdbcTemplate.query(query, new JdbcBoardDao.BoardRespMapper(), id, paging.getStartIndex(), paging.getEndIndex());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Integer getTotalCnt() {
        String query = "SELECT count(*) FROM board";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    // 게시물 전체 가져오기
    public List<BoardResponse> selectPost(Paging paging) {
//        String query = "SELECT b.board_no, b.title, b.content, b.user_idx, u.name, b.create_date, b.update_date FROM board b inner join user u on b.user_idx = u.idx ORDER BY b.board_no ASC LIMIT 10 OFFSET ?";
        String query = "SELECT board_no, title, content, user_idx, name, create_date, update_date " +
                "FROM" +
                "(SELECT ROW_NUMBER() OVER ( ORDER BY board_no ASC) AS row_num, board_no, title, content, user_idx, create_date, update_date " +
                "FROM board) b " +
                "INNER JOIN user u " +
                "ON b.user_idx = u.idx" +
                "WHERE row_num BETWEEN ? AND ? " +
                "ORDER BY row_num";
        try {
            return jdbcTemplate.query(query, new JdbcBoardDao.BoardRespMapper(),paging.getStartIndex(), paging.getEndIndex());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public BoardResponse searchPosts(String q) {
        return null;
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
        String query = "UPDATE board SET title = ?, content = ?, update_date = ? " +
                "WHERE board_no = ? " ;
        jdbcTemplate.update(query, boardDto.getTitle(), boardDto.getContent(), boardDto.getUpdateTime(), boardDto.getBoardNo());
    }

    // 게시물 삭제
    public void deletePost(Integer bIdx){
        String query = "DELETE FROM board WHERE board_no = ?" ;
        jdbcTemplate.update(query, bIdx);
    }
}
