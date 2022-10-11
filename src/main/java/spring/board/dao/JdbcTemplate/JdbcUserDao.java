package spring.board.dao.JdbcTemplate;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.board.dto.UserRequest;
import spring.board.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcUserDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 사용자 등록
    public void insertUser(UserRequest userRequest){
        String query = "INSERT INTO user (name, id, password, email) values (?, ?, ?, ?)" ;
        jdbcTemplate.update(query, userRequest.getName(), userRequest.getId(), userRequest.getPassword(), userRequest.getEmail());
    }

    // 사용자 정보 가져오기
    public User selectUser(String id) {
        String query = "SELECT * FROM user WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
        return null;
        }
    }

    public class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getInt("idx"),
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email")
            );
        }
    }

}
