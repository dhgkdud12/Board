package spring.board.dao.JdbcTemplate;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spring.board.dto.FileRequest;
import spring.board.entity.FileEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcFileDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcFileDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 파일 등록
    public int insertFile(FileEntity file){
        String query = "INSERT INTO file (board_no, file_name, convert_name, path, extension, size) values (?, ?, ?, ?, ?, ?)" ;
        return jdbcTemplate.update(query, file.getBoardNo(), file.getFileName(), file.getConvertName(), file.getPath(), file.getExtension(), file.getSize());
    }

    // 파일 가져오기
    public FileRequest selectFile(Integer fIdx) {
        String query = "SELECT * FROM file WHERE file_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcFileDao.FileRowMapper(), fIdx);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    // 게시물 id에 대한 파일 가져오기
    public FileRequest selectFileByBoardId(Integer bIdx) {
        String query = "SELECT * FROM file WHERE board_no = ?";
        try {
            return jdbcTemplate.queryForObject(query, new JdbcFileDao.FileRowMapper(), bIdx);
        } catch (Exception e) {
            return null;
        }
    }


    public class FileRowMapper implements RowMapper<FileRequest> {
        @Override
        public FileRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FileRequest(
                    rs.getInt("file_no"),
                    rs.getInt("board_no"),
                    rs.getString("file_name"),
                    rs.getString("convert_name"),
                    rs.getString("path"),
                    rs.getString("extension"),
                    rs.getLong("size")
                    );
        }
    }
}
