package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.dto.file.FileRequest;
import spring.board.domain.FileEntity;

@Mapper
public interface FileMapper {
    int insertFile(FileEntity file);

    FileRequest selectFile(Integer fIdx);

    FileRequest selectFileByBoardId(Integer bIdx);


}
