package spring.board.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.board.dto.LoginDto;
import spring.board.dto.UserDto;
import spring.board.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Repository
public class JdbcUserDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 사용자 등록
    public void insertUser(UserDto userDto){
        String query = "INSERT INTO user (name, id, password, email) values (?, ?, ?, ?)" ;
        jdbcTemplate.update(query, userDto.getName(), userDto.getId(), userDto.getPassword(), userDto.getEmail());
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
