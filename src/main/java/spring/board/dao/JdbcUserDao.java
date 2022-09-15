package spring.board.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import spring.board.dto.UserDto;

@Repository
public class JdbcUserDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 사용자 등록
    public void insertUser(UserDto userDto) {
        String query = "INSERT INTO user (name, id, password, email) values (?, ?, ?, ?)" ;
        jdbcTemplate.update(query, userDto.getName(), userDto.getId(), userDto.getPassword(), userDto.getEmail());
    }

//    public class AnimalRowMapper implements RowMapper<Animal> {
//        @Override
//        public Animal mapRow(ResultSet rs, int rowNum) throws SQLException {
//            return new Animal(
//                    rs.getInt("id"),
//                    rs.getString("name"),
//                    rs.getString("type"),
//                    rs.getInt("age")
//            );
//        }
//    }
//
//    // 사용자 모두 가져오기
//    public List<Animal> selectAllAnimals() {
//        String query = "SELECT * FROM Animal";
////        return jdbcTemplate.query(query, new BeanPropertyRowMapper<Animal>(Animal.class));
//        return jdbcTemplate.query(query,
//                (rs, rowNum) -> new Animal(
//                        rs.getInt("id"),
//                        rs.getString("name"),
//                        rs.getString("type"),
//                        rs.getInt("age")
//                )
//        );
//    }
}
