package spring.board.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.board.entity.FileEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcFileDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcFileDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 게시물 등록
    public int insertFile(FileEntity file){
        String query = "INSERT INTO file (board_no, file_name, convert_name, path, extension) values (?, ?, ?, ?, ?)" ;
        return jdbcTemplate.update(query, file.getBoardNo(), file.getFileName(), file.getConvertName(), file.getPath(), file.getExtention());
    }

    public FileEntity selectFile(Integer fIdx) {
        String query = "SELECT * FROM file WHERE file_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcFileDao.FileRowMapper(), fIdx);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public class FileRowMapper implements RowMapper<FileEntity> {
        @Override
        public FileEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FileEntity(
                    rs.getInt("file_no"),
                    rs.getInt("board_no"),
                    rs.getString("file_name"),
                    rs.getString("convert_name"),
                    rs.getString("path"),
                    rs.getString("extension")
                    );
        }
    }
}
